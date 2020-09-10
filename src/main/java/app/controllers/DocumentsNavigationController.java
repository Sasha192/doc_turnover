package app.controllers;

import static org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;

import app.configuration.spring.constants.Constants;
import app.controllers.dto.EmailDto;
import app.models.basic.Performer;
import app.models.mysqlviews.BriefJsonDocument;
import app.security.models.SimpleRole;
import app.security.wrappers.IPerformerWrapper;
import app.service.extapis.GMailService;
import app.service.interfaces.IBriefDocumentService;
import app.service.interfaces.IBriefJsonDocumentService;
import app.utils.CustomAppDateTimeUtil;
import app.utils.DocumentsUploader;
import app.utils.ZipUtils;
import app.utils.exceptions.MaliciousFoundException;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FilenameUtils;
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


// @TODO : Handle Exceptions!!!
@Controller
@RequestMapping("archive/doc")
public class DocumentsNavigationController extends JsonSupportController {

    private static final String IOEXCEPTION_WHILE_SENDING_DATA_ =
            "IOEXCEPTION WHILE SENDING DATA ";

    private static final String
            FILE_NOT_FOUND_EXC =
            "FILENOTFOUNDEXCEPTION WHILE TRYING TO FIND DOCUMENT IN FILESYSTEM";

    private static GsonBuilder builder = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat(Constants.DATE_FORMAT.toPattern());

    private final Constants constants;

    private GMailService mailService;

    private IBriefDocumentService docService;

    private IBriefJsonDocumentService jsonDocService;

    private IPerformerWrapper performerWrapper;

    private final DocumentsUploader uploader;

    private final DocRetrieveUtils docRetrieveUtils;

    @Autowired
    public DocumentsNavigationController(GMailService mailService,
                                         IBriefDocumentService docService,
                                         IBriefJsonDocumentService jsonDocService,
                                         IPerformerWrapper performerWrapper,
                                         @Qualifier("app_constants") Constants constants,
                                         @Qualifier("doc_uploader") DocumentsUploader uploader,
                                         DocRetrieveUtils docRetrieveUtils) {
        this.mailService = mailService;
        this.docService = docService;
        this.jsonDocService = jsonDocService;
        this.performerWrapper = performerWrapper;
        this.constants = constants;
        this.uploader = uploader;
        this.docRetrieveUtils = docRetrieveUtils;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(final HttpServletResponse response, final HttpServletRequest request,
                     @RequestParam(name = "page_id") @NotNull final Integer pageId)
            throws IOException {
        final String year = request.getParameter("year");
        final String month = request.getParameter("month");
        final String day = request.getParameter("date");
        if ((day != null && day.length() > 2)
                || (month != null && month.length() > 2)
                || (year != null && year.length() > 6)) {
            sendDefaultJson(response, false, "DATE NOT FOUND OR TOO BIG VALUE");
            return;
        }
        Integer yearInt = null;
        Integer monthInt = null;
        Integer dayInt = null;
        try {
            yearInt = year == null ? null : Integer.parseInt(year);
            monthInt = month == null ? null : Integer.parseInt(month);
            dayInt = day == null ? null : Integer.parseInt(day);
        } catch (final NumberFormatException e) {
            sendDefaultJson(response, false, "");
            this.getExceptionLogger().error("ERROR WHILE INTEGER PARSING ", e);
            return;
        }
        final String search = request.getParameter("search");
        Performer performer = performerWrapper.retrievePerformer(request);
        Set<SimpleRole> roles = performer.getRoles();
        List<BriefJsonDocument> list = null;
        if (allowListArchive(roles)) {
            list = this.jsonDocService
                    .findBy(pageId, search, yearInt, monthInt, dayInt);
            writeToResponse(response, builder, list);
        } else {
            if (roles.contains(SimpleRole.MANAGER)) {
                list = jsonDocService.findByAndDepartment(pageId, search, yearInt, monthInt,
                        dayInt, performer.getDepartmentId());
            } else {
                list = jsonDocService.findByAndPerformerInTaskId(pageId, search,
                        yearInt, monthInt, dayInt, performer.getId());
            }
            writeToResponse(response, builder, list);
        }
    }

    private boolean allowListArchive(Set<SimpleRole> roles) {
        return roles.contains(SimpleRole.ADMIN)
                || roles.contains(SimpleRole.G_MANAGER)
                || roles.contains(SimpleRole.SECRETARY);
    }

    private boolean allowUploadArchive(Set<SimpleRole> roles) {
        return roles.contains(SimpleRole.G_MANAGER)
                || roles.contains(SimpleRole.ADMIN)
                || roles.contains(SimpleRole.SECRETARY);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(@RequestParam("file") final MultipartFile[] mfiles,
                       final HttpServletResponse response,
                       final HttpServletRequest req) {
        if (mfiles.length > constants.get(Constants.MAX_FILES_UPLOAD).getIntValue()) {
            sendDefaultJson(
                    response, false,
                    "Too many files. You can upload no more " + constants
                            .get(Constants.MAX_FILES_UPLOAD)
                            .getIntValue()
            );
            return;
        }
        Performer performer = performerWrapper.retrievePerformer(req);
        Set<SimpleRole> roles = performer.getRoles();
        if (allowUploadArchive(roles)) {
            try {
                uploader.upload(performer, mfiles);
            } catch (MaliciousFoundException ioex) {
                sendDefaultJson(response, false, ioex.getMessage());
            } catch (IOException e) {
                sendDefaultJson(response, false, "Internal Server Error. Please try later.");
            }
            sendDefaultJson(response, true, "");
        } else {
            sendDefaultJson(response, false, "Access Denied");
        }
    }

    @RequestMapping(path = "/download",
            method = RequestMethod.GET)
    /**
     * @TODO : Anyboudy could download any file -> FIX IT!
     */
    public void download(@RequestParam(value = "id") final String[] docIds,
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
            final File[] files = docRetrieveUtils.retrieveFilesByDocIds(docIds);
            if (files.length == 1) {
                this.sendFile(response, files[0]);
            } else {
                this.sendFile(response, files);
            }
        } catch (NumberFormatException ex) {
            sendDefaultJson(response, false, "Illegal operation");
        } catch (FileNotFoundException fnx) {
            String message = FILE_NOT_FOUND_EXC;
            getExceptionLogger().error(message, fnx);
            sendDefaultJson(response, false, message);
        } catch (final IOException e) {
            String message = IOEXCEPTION_WHILE_SENDING_DATA_ + "\n" + e;
            this.getExceptionLogger().error(message);
            sendDefaultJson(response, false, "");
        }
    }

    @RequestMapping(path = "/send",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    /**
     * @TODO : Anyboudy could send any file -> FIX IT!
     */
    public void sendFile(@Validated @RequestBody EmailDto emailDto,
                         final HttpServletResponse response,
                         HttpServletRequest req) {
        Performer performer = performerWrapper.retrievePerformer(req);
        try {
            final File[] files = docRetrieveUtils.retrieveFilesByDocIds(emailDto.getDocList());
            boolean responseBool = false;
            if (files.length > 0) {
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

    private void sendFile(final HttpServletResponse response, final File file)
            throws IOException {
        final InputStream in = new FileInputStream(file);
        final String extesion = FilenameUtils.getExtension(file.getAbsolutePath());
        String contentType = Constants.CONTENT_TYPE_MAP
                .getContentTypeFor(Constants.DOT.concat(extesion));
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        contentType = contentType.concat("; charset=UTF-8");
        response.setContentType(contentType);
        response.setHeader("Content-disposition", String.format("attachment; filename=%s",
                URLEncoder.encode(file.getName(), StandardCharsets.UTF_8)));
        final int dataSize = Math.toIntExact(file.length());
        response.setContentLength(dataSize);
        final OutputStream out = response.getOutputStream();
        IOUtils.copy(in, out);
        out.flush();
        out.close();
        in.close();
    }

    private void sendFile(final HttpServletResponse response, final File... files)
            throws IOException {
        if (files.length == 0) {
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
        ZipUtils.write(zipOut, files);
        zipOut.finish();
        zipOut.close();
    }
}
