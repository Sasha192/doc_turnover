package app.controllers;

import static org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;

import app.configuration.spring.constants.Constants;
import app.controllers.utils.RunnableDatabaseStore;
import app.models.BriefDocument;
import app.models.BriefJsonDocument;
import app.models.Performer;
import app.security.utils.PerformerWrapper;
import app.service.IBriefDocumentService;
import app.service.IBriefJsonDocumentService;
import app.service.extapis.GMailService;
import app.service.extapis.VirusTotalScan;
import app.service.impl.ExecutionService;
import com.google.gson.GsonBuilder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


// @TODO : Handle Exceptions!!!
@Controller
@RequestMapping("archive/doc")
public class DocumentsNavigationController extends JsonSupportController {

    private static final String IOEXCEPTION_WHILE_SENDING_DATA_ = "IOEXCEPTION WHILE SENDING DATA ";

    private static final String
            FILE_NOT_FOUND_EXC =
            "FILENOTFOUNDEXCEPTION WHILE TRYING TO FIND DOCUMENT IN FILESYSTEM. FILENAME = ";

    private static final String MAX_FILES_UPLOAD = "max_files_upload";

    private static final String MAX_FILES_DOWNLOAD = "max_files_download";

    @Autowired
    @Qualifier("app_constants")
    private Constants constants;

    private VirusTotalScan virusTotalScan;

    private GMailService mailService;

    private IBriefDocumentService docService;

    private IBriefJsonDocumentService jsonDocService;

    private PerformerWrapper performerWrapper;

    private ExecutionService executionService;

    @Autowired
    public DocumentsNavigationController(VirusTotalScan virusTotalScan,
                                         GMailService mailService,
                                         IBriefDocumentService docService,
                                         IBriefJsonDocumentService jsonDocService,
                                         PerformerWrapper performerWrapper,
                                         ExecutionService executionService) {
        this.virusTotalScan = virusTotalScan;
        this.mailService = mailService;
        this.docService = docService;
        this.jsonDocService = jsonDocService;
        this.performerWrapper = performerWrapper;
        this.executionService = executionService;
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
            this.sendDefaultJson(response, false, "DATE NOT FOUND OR TOO BIG VALUE");
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
            this.getExceptionLogger().error("ERROR WHILE INTEGER PARSING ", e);
        }
        final String search = request.getParameter("search");
        final List<BriefJsonDocument> list = this.jsonDocService.findBy(pageId,
                search,
                yearInt,
                monthInt,
                dayInt);
        final GsonBuilder builder = new GsonBuilder()
                .setPrettyPrinting();
        this.writeToResponse(response, builder, list);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(@RequestParam("file") final MultipartFile[] mfiles,
                       final HttpServletResponse response) throws IOException {
        if (mfiles.length > constants.get(MAX_FILES_UPLOAD).getIntValue()) {
            sendDefaultJson(
                    response, false,
                    "Too many files. You can upload no more " + constants
                            .get(MAX_FILES_UPLOAD)
                            .getIntValue()
            );
            return;
        }
        final LocalDate now = LocalDate.now();
        final int year = now.getYear();
        final int month = now.getMonthValue();
        final int day = now.getDayOfMonth();
        final String filePath;
        filePath = this.constants.get("path_to_archive")
                .getStringValue()
                + (Constants.SLASH + year)
                + (Constants.SLASH + month)
                + (Constants.SLASH + day);
        final File fileFolder = new File(filePath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        final List<File> files = new LinkedList<>();
        boolean success = true;
        String msg = Constants.EMPTY_STRING;
        for (int i = 0; i < mfiles.length; i++) {
            final MultipartFile mfile = mfiles[i];
            String fileName = new String(
                    mfile.getOriginalFilename().getBytes(
                            StandardCharsets.ISO_8859_1
                    ),
                    StandardCharsets.UTF_8
            );
            // @TODO : Change this for different Charsets.
            //  e.g. There was problem with CharsetDetector(CD)
            //  CD works incorrectly!!!
            // @TODO :
            final File fileToSave = new File(
                    filePath.concat(
                            Constants.SLASH.concat(fileName)
                    )
            );
            mfile.transferTo(fileToSave);
            files.add(fileToSave);
            if (!this.virusTotalScan.scan(fileToSave)) {
                success = false;
                msg = "File : ".concat(mfile.getOriginalFilename())
                        .concat(Constants.IS_MALICIOUS);
                break;
            }
        }
        if (!success) {
            this.removeAllFiles(files);
            this.sendDefaultJson(response, success, msg);
            return;
        }
        Performer performer = this.performerWrapper.retrievePerformer();
        Runnable runnable = new RunnableDatabaseStore(
                files, this.docService,
                filePath, performer
        );
        this.executionService.pushTask(runnable);
        this.sendDefaultJson(response, true, "");
    }

    private void removeAllFiles(final List<File> files) throws IOException {
        for (final File file : files) {
            if (!file.delete()) {
                FileDeleteStrategy.FORCE.delete(file);
            }
        }
    }

    @RequestMapping(path = "/download",
            method = RequestMethod.GET)
    public void download(@RequestParam("id") final String[] docIds,
                         final HttpServletResponse response) {
        if (docIds.length > constants.get(MAX_FILES_DOWNLOAD).getIntValue()) {
            sendDefaultJson(
                    response, false,
                    "You could not download more than "
                            + constants.get(MAX_FILES_DOWNLOAD).getIntValue()
            );
            return;
        }
        final File[] files = this.retrieveFilesByDocIds(docIds);
        if (files != null) {
            if (files.length == 1) {
                this.sendFile(response, files[0]);
            } else {
                this.sendFile(response, files);
            }
        }
    }

    private File[] retrieveFilesByDocIds(final String[] docIds) {
        final File[] files = new File[docIds.length];
        int i = 0;
        for (final String docId : docIds) {
            final Long id = Long.valueOf(docId);
            final BriefDocument briefDocument = this.docService.findOne(id);
            final String filePath = briefDocument.getPath()
                    .concat(Constants.SLASH)
                    .concat(briefDocument.getName())
                    .concat(briefDocument.getExtName());
            files[i++] = new File(filePath);
        }
        return files;
    }

    @RequestMapping(path = "/send",
            method = RequestMethod.GET)
    public void sendFile(@RequestParam("id") final String[] docId,
                         @RequestParam("msg") final String msg,
                         @RequestParam("to") final String to,
                         @RequestParam("subject") final String subject,
                         final HttpServletResponse response) {
        final File[] files = this.retrieveFilesByDocIds(docId);
        boolean responseBool = false;
        if (files != null && files.length > 0) {
            responseBool = this.mailService
                    .sendFile(to, subject, msg, files);
        }
        this.sendDefaultJson(response, responseBool, "");
    }

    private void sendFile(final HttpServletResponse response, final File file) {
        try (final InputStream in = new FileInputStream(file)) {
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
            final BufferedOutputStream bufOut = new BufferedOutputStream(out);
            int bytesRead = 0;
            final BufferedInputStream bufIn = new BufferedInputStream(in);
            final byte[] buf = new byte[2048];
            while ((bytesRead = bufIn.read(buf)) != -1) {
                bufOut.write(buf, 0, bytesRead);
            }
            bufOut.flush();
            bufOut.close();
            bufIn.close();
        } catch (final FileNotFoundException e) {
            this.getExceptionLogger()
                    .error(FILE_NOT_FOUND_EXC + file.getAbsolutePath(), e);
        } catch (final IOException e) {
            this.getExceptionLogger()
                    .error(IOEXCEPTION_WHILE_SENDING_DATA_, e);
        }
    }

    private void sendFile(final HttpServletResponse response, final File... files) {
        if (files.length == 0) {
            return;
        }
        try {
            final ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(
                    response.getOutputStream()
            );
            zipOut.setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy.ALWAYS);
            response.setContentType("application/zip;charset=UTF-8");
            final String zipName = Constants.DATE_FORMAT.format(Date.valueOf(LocalDate.now()));
            response.setHeader("Content-Disposition", MessageFormat
                    .format("attachment; filename=\"{0}.zip\"",
                            URLEncoder.encode(zipName, StandardCharsets.UTF_8)));
            final byte[] buf = new byte[2048];
            for (final File file : files) {
                try {
                    final InputStream in = new FileInputStream(file);
                    final String entryname = file.getName();
                    zipOut.putArchiveEntry(new ZipArchiveEntry(entryname));
                    IOUtils.copy(in, zipOut);
                    zipOut.flush();
                    in.close();
                    zipOut.closeArchiveEntry();
                } catch (final FileNotFoundException e) {
                    this.getExceptionLogger()
                            .error(FILE_NOT_FOUND_EXC + file.getAbsolutePath(), e);
                } catch (final IOException e) {
                    this.getExceptionLogger()
                            .error(IOEXCEPTION_WHILE_SENDING_DATA_, e);
                }
            }
            zipOut.finish();
            zipOut.close();
        } catch (final IOException e) {
            this.getExceptionLogger().error(IOEXCEPTION_WHILE_SENDING_DATA_, e);
        }
    }
}
