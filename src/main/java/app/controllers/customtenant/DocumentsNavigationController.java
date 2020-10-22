package app.controllers.customtenant;

import static org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;

import app.configuration.spring.constants.Constants;
import app.controllers.dto.EmailDto;
import app.customtenant.models.basic.BriefDocument;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.extapis.GMailService;
import app.customtenant.service.interfaces.IBriefDocumentService;
import app.security.models.SimpleRole;
import app.security.wrappers.IPerformerWrapper;
import app.tenantdefault.models.DocumentEntity;
import app.tenantdefault.service.IDocumentStorage;
import app.utils.ContentTypeFileUtil;
import app.utils.CustomAppDateTimeUtil;
import app.utils.DocumentsUploader;
import app.utils.ZipUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("archive/doc")
public class DocumentsNavigationController extends JsonSupportController {

    private static final String IOEXCEPTION_WHILE_SENDING_DATA_ =
            "IOEXCEPTION WHILE SENDING DATA ";

    private static GsonBuilder builder = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat(Constants.DATE_FORMAT.toPattern());

    private final Constants constants;

    private GMailService mailService;

    private IBriefDocumentService docService;

    private IPerformerWrapper performerWrapper;

    private final DocumentsUploader uploader;

    private final IDocumentStorage documentStorage;

    @Autowired
    public DocumentsNavigationController(GMailService mailService,
                                         IBriefDocumentService docService,
                                         IPerformerWrapper performerWrapper,
                                         @Qualifier("app_constants") Constants constants,
                                         @Qualifier("doc_uploader") DocumentsUploader uploader,
                                         IDocumentStorage documentStorage) {
        this.mailService = mailService;
        this.docService = docService;
        this.performerWrapper = performerWrapper;
        this.constants = constants;
        this.uploader = uploader;
        this.documentStorage = documentStorage;
    }

    @RequestMapping(value = "/list",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void list(final HttpServletResponse response, final HttpServletRequest request,
                     @RequestParam(name = "page_id") @NotNull final Integer pageId,
                     @RequestBody String body)
            throws IOException {
        JsonElement element = JsonParser.parseString(body);
        if (!element.isJsonObject()) {
            sendDefaultJson(response, false, "");
        }
        Date date = null;
        Long startTime = null;
        Long endTime = null;
        String word = null;
        try {
            JsonObject json = element.getAsJsonObject();
            if (json.has("date")) {
                String dateStr = json.get("date").getAsString();
                date = Constants.DATE_FORMAT.parse(dateStr);
            }
            if (json.has("start_time") && json.has("end_time")) {
                String startTimeStr = json.get("start_time").getAsString();
                String endTimeStr = json.get("end_time").getAsString();
                startTime = Long.valueOf(startTimeStr);
                endTime = Long.valueOf(endTimeStr);
            }
            if (json.has("search_word")) {
                word = json.get("search_word").getAsString();
            }
        } catch (ParseException | NumberFormatException ignored) {
            ;
        }
        Performer performer = performerWrapper.retrievePerformer(request);
        SimpleRole roles = performer.getRoles();
        List<BriefDocument> list = null;
        if (allowListArchive(roles)) {
            list = docService.findBy(pageId, date, startTime, endTime, word);
        } else {
            if (roles.equals(SimpleRole.MANAGER)) {
                list = docService.findByAndDepartment(pageId, date,
                        performer.getDepartment().getId());
            } else {
                list = docService.findByAndPerformerInTaskId(pageId, date, performer.getId());
            }
        }
        writeToResponse(response, builder, list);
    }

    private boolean allowListArchive(SimpleRole roles) {
        return roles.equals(SimpleRole.ADMIN)
                || roles.equals(SimpleRole.G_MANAGER)
                || roles.equals(SimpleRole.SECRETARY);
    }

    private boolean allowUploadArchive(SimpleRole roles) {
        return roles.equals(SimpleRole.G_MANAGER)
                || roles.equals(SimpleRole.ADMIN)
                || roles.equals(SimpleRole.SECRETARY);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(@RequestParam("file") final MultipartFile[] mfiles,
                       final HttpServletResponse response,
                       final HttpServletRequest req) {
        if (mfiles.length > constants.get(Constants.MAX_FILES_UPLOAD).getIntValue()) {
            sendDefaultJson(response, false,
                    "Too many files. You can upload no more " + constants
                            .get(Constants.MAX_FILES_UPLOAD)
                            .getIntValue()
            );
            return;
        }
        try {
            Performer performer = performerWrapper.retrievePerformer(req);
            SimpleRole roles = performer.getRoles();
            if (allowUploadArchive(roles)) {
                uploader.upload(performer, mfiles);
                sendDefaultJson(response, true, "");
            } else {
                sendDefaultJson(response, false, "Access Denied");
            }
        } catch (IOException e) {
            this.getExceptionLogger()
                    .error(DocumentsNavigationController.class.getName()
                            + " EXCEPTION", e);
            sendDefaultJson(response, false, "Please try later.");
        }
    }

    @RequestMapping(path = "/download",
            method = RequestMethod.GET)
    public void download(@RequestParam(value = "uuid") final String[] docIds,
                         final HttpServletResponse response) {
        if (docIds.length > constants.get(Constants.MAX_FILES_DOWNLOAD).getIntValue()) {
            sendDefaultJson(
                    response, false,
                    "You could not download more than "
                            + constants.get(Constants.MAX_FILES_DOWNLOAD).getIntValue()
            );
            return;
        }
        try {
            final List<DocumentEntity> files = retrieveFilesByDocIds(docIds);
            if (!files.isEmpty()) {
                if (files.size() == 1) {
                    this.sendFile(response, files.get(0));
                } else {
                    this.sendFile(response, files);
                }
            }
        } catch (NumberFormatException ex) {
            sendDefaultJson(response, false, "");
        } catch (FileNotFoundException fnx) {
            sendDefaultJson(response, false, "");
        } catch (final IOException e) {
            sendDefaultJson(response, false, "");
        }
    }

    private List<DocumentEntity> retrieveFilesByDocIds(String[] docIds)
            throws NumberFormatException {
        Long[] ids = new Long[docIds.length];
        int i = 0;
        for (String docId : docIds) {
            ids[i++] = Long.valueOf(docId);
        }
        List<BriefDocument> documents = docService.findSeveralById(ids);
        List<DocumentEntity> entities = new LinkedList<>();
        for (BriefDocument doc : documents) {
            entities.add(documentStorage.find(doc.getUuid()));
        }
        return entities;
    }

    @RequestMapping(path = "/send",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendFile(@Validated @RequestBody EmailDto emailDto,
                         final HttpServletResponse response,
                         HttpServletRequest req) {
        Performer performer = performerWrapper.retrievePerformer(req);
        if (!allowUploadArchive(performer.getRoles())) {
            sendDefaultJson(response, false, "ACCESS DENIED");
        }
        try {
            final List<DocumentEntity> files = retrieveFilesByDocIds(emailDto.getDocList());
            boolean responseBool = false;
            if (files.size() > 0) {
                responseBool = this.mailService
                        .sendFile(emailDto.getEmail(),
                                "Новий лист від " + performer.getName(),
                                emailDto.getMessage(),
                                files
                        );
            }
            sendDefaultJson(response, responseBool, "");
        } catch (NumberFormatException ex) {
            getExceptionLogger().error(ex);
            sendDefaultJson(response, false, "Illegal operation");
        }
    }

    private void sendFile(final HttpServletResponse response, final DocumentEntity entity)
            throws IOException {
        byte[] data = entity.getDocument().getData();
        final InputStream in = new ByteArrayInputStream(data);
        String contentType = ContentTypeFileUtil.getContentType(entity.getName());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        contentType = contentType.concat("; charset=UTF-8");
        response.setContentType(contentType);
        response.setHeader("Content-disposition", String.format("attachment; filename=%s",
                URLEncoder.encode(entity.getName(), StandardCharsets.UTF_8)));
        final int dataSize = data.length;
        response.setContentLength(dataSize);
        final OutputStream out = response.getOutputStream();
        IOUtils.copy(in, out);
        out.flush();
        out.close();
        in.close();
    }

    private void sendFile(final HttpServletResponse response, final List<DocumentEntity> entities)
            throws IOException {
        if (entities.isEmpty()) {
            return;
        }
        final ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(
                response.getOutputStream()
        );
        zipOut.setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy.ALWAYS);
        response.setContentType("application/zip;charset=UTF-8");
        final String zipName = Constants.DATE_FORMAT.format(CustomAppDateTimeUtil.now());
        response.setHeader("Content-Disposition", MessageFormat
                .format("attachment; filename=\"{0}.zip\"",
                        URLEncoder.encode(zipName, StandardCharsets.UTF_8)));
        ZipUtils.write(zipOut, entities);
        zipOut.finish();
        zipOut.close();
    }
}
