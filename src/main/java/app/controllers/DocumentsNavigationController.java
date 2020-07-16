package app.controllers;

import app.configuration.spring.constants.Constants;
import app.controllers.utils.RunnableDatabaseStore;
import app.dao.IBriefDocumentJsonDao;
import app.models.BriefDocument;
import app.models.BriefJsonDocument;
import app.models.Performer;
import app.security.utils.PerformerWrapper;
import app.service.IBriefDocumentService;
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
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "archive/doc")
public class DocumentsNavigationController extends JsonSupportController {

    private static final String IOEXCEPTION_WHILE_SENDING_DATA_ = "IOEXCEPTION WHILE SENDING DATA ";
    private static final String
            FILENOTFOUNDEXCEPTION_WHILE_TRYING_TO_FIND_DOCUMENT_IN_FILESYSTEM_FILENAME =
            "FILENOTFOUNDEXCEPTION WHILE TRYING TO FIND DOCUMENT IN FILESYSTEM. FILENAME = ";


    /*private static final String PERF_ATTR = "performer_object".intern();

    private static final String EXTENSION_FILE = ".docx";

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd_MM_yyyy");
import javax.annotation.PostConstruct;
    private static final String SEPARATOR_FILE_SYSTEM = null;

    private static final String FILE_PATH_SEPARATOR = File.separator;

    private static final String UNDERSCORE = "_";

    private static final Logger LOGGER = Logger.getLogger(TaskNavigationController.class);

    private static final String FILE_TRANSFER_ERROR =
            "==================FILE TRANSFER ERROR==================";*/

    @Autowired
    @Qualifier("app_constants")
    private Constants constants;

    @Autowired
    private VirusTotalScan virusTotalScan;

    @Autowired
    private GMailService mailService;

    @Autowired
    private IBriefDocumentService docService;

    @Autowired
    private IBriefDocumentJsonDao jsonDocService;

    @Autowired
    private PerformerWrapper performerWrapper;

    @Autowired
    private ExecutionService executionService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(HttpServletResponse response, HttpServletRequest request,
                     @RequestParam(name = "page_id") @NotNull Integer pageId)
            throws IOException {
        // throws IOException to the top,
        // because then it is a problem with (closed response.getWriter())
        // Controller should not deal with it
        // @TODO
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String day = request.getParameter("date");
        if ((day != null && day.length() > 2)
                || (month != null && month.length() > 2)
                || (year != null && year.length() > 6)) {
            sendDefaultJson(response, false, "DATE NOT FOUND OR TOO BIG VALUE");
        }
        Integer yearInt = null;
        Integer monthInt = null;
        Integer dayInt = null;
        try {
            yearInt = year == null ? null : Integer.parseInt(year);
            monthInt = month == null ? null : Integer.parseInt(month);
            dayInt = day == null ? null : Integer.parseInt(day);
        } catch (NumberFormatException e) {
            getExceptionLogger().error("ERROR WHILE INTEGER PARSING ", e);
        }
        String search = request.getParameter("search");
        List<BriefJsonDocument> list = jsonDocService.findBy(pageId, search, yearInt, monthInt, dayInt);
        GsonBuilder builder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting();
        writeToResponse(response, builder, list);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(@RequestParam("file") MultipartFile[] mfiles,
                       HttpServletResponse response) throws IOException {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        String filePath = constants.getPathToArchive().concat(Constants.SLASH + year)
                .concat(Constants.SLASH + month)
                .concat(Constants.SLASH + day);
        File fileFolder = new File(filePath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        // @TODO : CAN WE DO IT ANOTHER THREADS ?
        //  I mean, VTScanning could be performed in several threads
        //  Or leave it ???!!!
        List<File> files = new LinkedList<>();
        boolean success = true;
        String msg = Constants.EMPTY_STRING;
        for (int i = 0; i < mfiles.length; i++) {
            MultipartFile mfile = mfiles[i];
            File fileToSave = new File(filePath.concat(
                    Constants.SLASH.concat(mfile.getOriginalFilename()))
            );
            mfile.transferTo(fileToSave);
            files.add(fileToSave);
            if (!virusTotalScan.scan(fileToSave)) {
                success = false;
                msg = "File : ".concat(mfile.getOriginalFilename()).concat(Constants.IS_MALICIOUS);
                break;
            }
        }
        if (!success) {
            removeAllFiles(files);
            sendDefaultJson(response, success, msg);
            return;
        }
        Performer performer = performerWrapper.retrievePerformer();
        Runnable runnable = new RunnableDatabaseStore(
                files, docService,
                filePath, performer
        );
        executionService.pushTask(runnable);
        sendDefaultJson(response, true, "");
    }

    private void removeAllFiles(List<File> files) throws IOException {
        for (File file : files) {
            if (!file.delete()) {
                FileDeleteStrategy.FORCE.delete(file);
            }
        }
    }

    @RequestMapping(path = "/download",
            method = RequestMethod.GET)
    public void download(@RequestParam("id") String[] docIds,
                         HttpServletResponse response) {
        File[] files = retrieveFilesByDocIds(docIds);
        if (files != null) {
            if (files.length == 1) {
                sendFile(response, files[0]);
            } else {
                sendFile(response, files);
            }
        }
    }

    private File[] retrieveFilesByDocIds(String[] docIds) {
        File[] files = new File[docIds.length];
        int i = 0;
        for (String docId : docIds) {
            Long id = Long.valueOf(docId);
            BriefDocument briefDocument = docService.findOne(id);
            String filePath = briefDocument.getPath()
                    .concat(Constants.SLASH)
                    .concat(briefDocument.getName())
                    .concat(briefDocument.getExtName());
            files[i++] = new File(filePath);
        }
        return files;
    }

    @RequestMapping(path = "/send",
            method = RequestMethod.GET)
    public void sendFile(@RequestParam("id") String[] docId,
                         @RequestParam("msg") String msg,
                         @RequestParam("to") String to,
                         @RequestParam("subject") String subject,
                         HttpServletResponse response) {
        File[] files = retrieveFilesByDocIds(docId);
        boolean responseBool = false;
        if (files != null && files.length > 0) {
            responseBool = mailService.sendFile(to, subject, msg, files);
        }
        sendDefaultJson(response, responseBool, "");
    }

    private void sendFile(HttpServletResponse response, File file) {
        try (InputStream in = new FileInputStream(file)) {
            String extesion = FilenameUtils.getExtension(file.getAbsolutePath());
            final String contentType = Constants.CONTENT_TYPE_MAP.getContentTypeFor(extesion);
            response.setContentType(contentType);
            response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
            int dataSize = Math.toIntExact(file.length());
            response.setContentLength(dataSize);
            OutputStream out = response.getOutputStream();
            BufferedOutputStream bufOut = new BufferedOutputStream(out);
            int bytesRead = 0;
            BufferedInputStream bufIn = new BufferedInputStream(in);
            byte[] buf = new byte[2048];
            while ((bytesRead = bufIn.read(buf)) != -1) {
                bufOut.write(buf, 0, bytesRead);
            }
            bufOut.flush();
            bufOut.close();
            bufIn.close();
        } catch (FileNotFoundException e) {
            getExceptionLogger().error(FILENOTFOUNDEXCEPTION_WHILE_TRYING_TO_FIND_DOCUMENT_IN_FILESYSTEM_FILENAME + file.getAbsolutePath(), e);
        } catch (IOException e) {
            getExceptionLogger().error(IOEXCEPTION_WHILE_SENDING_DATA_, e);
        }
    }

    private void sendFile(HttpServletResponse response, File... files) {
        if (files.length == 0) {
            return;
        }
        try {
            ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
            response.setContentType("application/zip");
            String zipName = Constants.DATE_FORMAT.format(Date.valueOf(LocalDate.now()));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + zipName + "\"");
            byte[] buf = new byte[2048];
            for (File file : files) {
                try {
                    InputStream in = new FileInputStream(file);
                    BufferedInputStream bufIn = new BufferedInputStream(in);
                    String entryname = file.getName();
                    zipOut.putNextEntry(new ZipEntry(entryname));
                    int bytesRead = 0;
                    while ((bytesRead = bufIn.read(buf)) != -1) {
                        zipOut.write(buf, 0, bytesRead);
                    }
                    zipOut.flush();
                    bufIn.close();
                    in.close();
                } catch (FileNotFoundException e) {
                    getExceptionLogger().error(FILENOTFOUNDEXCEPTION_WHILE_TRYING_TO_FIND_DOCUMENT_IN_FILESYSTEM_FILENAME + file.getAbsolutePath(), e);
                } catch (IOException e) {
                    getExceptionLogger().error(IOEXCEPTION_WHILE_SENDING_DATA_, e);
                }
            }
        } catch (IOException e) {
            getExceptionLogger().error(IOEXCEPTION_WHILE_SENDING_DATA_, e);
        }
    }
}
