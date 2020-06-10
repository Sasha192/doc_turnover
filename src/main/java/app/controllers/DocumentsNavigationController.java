package app.controllers;

import app.configuration.spring.constants.Constants;
import app.controllers.responses.ResponseJsonText;
import app.models.BriefArchive;
import app.models.BriefDocument;
import app.models.Performer;
import app.service.IBriefDocumentService;
import app.service.IPerformerService;
import app.service.extapis.GMailService;
import app.service.extapis.VirusTotalScan;
import com.google.gson.GsonBuilder;
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
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private Performer DEFAULT_PERFORMER;

    @Autowired
    @Qualifier("app.constants")
    private Constants constants;

    @Autowired
    private VirusTotalScan virusTotalScan;

    @Autowired
    private GMailService mailService;

    @Autowired
    private IBriefDocumentService service;

    @Autowired
    private IPerformerService performerService;

    @PostConstruct
    public void init() {
        DEFAULT_PERFORMER = performerService.findOne(1);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(HttpServletResponse response, HttpServletRequest request)
            throws IOException {
        String param = request.getParameter("status");
        List<BriefDocument> list = service.findAll();
        GsonBuilder builder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting();
        writeToResponse(response, builder, BriefArchive.toBriefArchive(list));
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
        List<File> files = new LinkedList<>();
        boolean success = true;
        String msg = Constants.EMPTY_STRING;
        for (int i = 0; i < mfiles.length; i++) {
            MultipartFile mfile = mfiles[i];
            File fileToSave = new File(filePath.concat(Constants.SLASH.concat(mfile.getOriginalFilename())));
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
        for (File file : files) {
            BriefDocument briefDocument = new BriefDocument();
            briefDocument.setPath(filePath);
            String fileName = file.getName();
            String fileNameWithoutExtension = FilenameUtils.removeExtension(fileName);
            briefDocument.setName(fileNameWithoutExtension);
            String filExtName = FilenameUtils.getExtension(fileName);
            briefDocument.setExtName(Constants.DOT.concat(filExtName));
            briefDocument.setDate(Date.valueOf(now));
            briefDocument.setPerformer(DEFAULT_PERFORMER); // @TODO REMOVE DEFAULT PERFORMER
            service.create(briefDocument);
        }
        sendDefaultJson(response, true, "");
    }

    private void removeAllFiles(List<File> files) {
        for (File file : files) {
            if (!file.delete()) {
                ; // @TODO in case it is not deleted -> it is in memory ?
            }
        }
    }

    private void sendDefaultJson(HttpServletResponse response, boolean b, String s) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
            writeToResponse(response, gsonBuilder, new ResponseJsonText(true, ""));
        } catch (IOException e) {
            ;
        }
    }

    @RequestMapping(path = "/send",
            method = RequestMethod.GET)
    public void sendFile(@RequestParam("id") String docId,
                         @RequestParam("msg") String msg,
                         @RequestParam("to") String to,
                         @RequestParam("subject") String subject,
                         HttpServletResponse response) {
        Long id = Long.valueOf(docId);
        BriefDocument briefDocument = service.findOne(id);
        String filePath = briefDocument.getPath()
                .concat(Constants.SLASH)
                .concat(briefDocument.getName())
                .concat(briefDocument.getExtName());
        File file = new File(filePath);
        mailService.sendFile(to, subject, msg, file);
        sendDefaultJson(response, true, "");
    }

    @RequestMapping(path = "/download",
            method = RequestMethod.GET)
    public void download(@RequestParam("id") String[] docIds,
                         HttpServletResponse response) {
        for (String docId : docIds) {
            Long id = Long.valueOf(docId);
            BriefDocument briefDocument = service.findOne(id);
            String filePath = briefDocument.getPath()
                    .concat(Constants.SLASH)
                    .concat(briefDocument.getName())
                    .concat(briefDocument.getExtName());
            File file = new File(filePath);
            sendData(response, file, briefDocument.getExtName());
        }

    }

    private void sendData(HttpServletResponse response, File file, String ext) {
        try (InputStream in = new FileInputStream(file)) {
            String contentType = Constants.CONTENT_TYPE_MAP.getContentTypeFor(ext);
            response.setContentType(contentType);
            response.setHeader("Content-disposition", "inline; filename=" + file.getName());
            int dataSize = Math.toIntExact(file.length());
            response.setContentLength(dataSize);
            OutputStream out = response.getOutputStream();
            int data = 0;
            while ((data = in.read()) != -1 && dataSize > 0) {
                out.write(data);
                dataSize--;
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            ;
        } catch (IOException e) {
            ;
        }
    }
}
