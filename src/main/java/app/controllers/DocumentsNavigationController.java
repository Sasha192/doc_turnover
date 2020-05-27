package app.controllers;

import app.models.BriefArchive;
import app.models.BriefDocument;
import app.service.IBriefDocumentService;
import app.service.MailService;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "archive/doc")
public class DocumentsNavigationController extends JsonSupportController {

    private static final String DOT = ".".intern();

    private static final String SLASH = "/".intern();

    private static final FileNameMap CONTENT_TYPE_MAP = URLConnection.getFileNameMap();

    private static String ROOT_PATH = "/home/kolmogorov/Java_Practice/bcrew/"
            + "doc_turnover/src/main/webapp/archive";

    @Autowired
    private MailService mailService;

    @Autowired
    private IBriefDocumentService service;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(HttpServletResponse response, HttpServletRequest request)
            throws IOException {
        String param = request.getParameter("status");
        List<BriefDocument> list = null;
        if (param != null) {
            switch (param) {
                case "done":
                    list = service.findArchived();
                    break;
                case "active":
                    list = service.findActive();
                    break;
                default:
                    break;
            }
        } else {
            list = service.findAll();
        }
        GsonBuilder builder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting();
        writeToResponse(response, builder, BriefArchive.toBriefArchive(list));
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        String filePath = ROOT_PATH.concat(SLASH + year)
                .concat(SLASH + month)
                .concat(SLASH + day);
        File fileFolder = new File(filePath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        try {
            File fileToSave = new File(filePath.concat(SLASH.concat(file.getOriginalFilename())));
            file.transferTo(fileToSave);
            BriefDocument briefDocument = new BriefDocument();
            briefDocument.setPath(filePath);
            String fileName = file.getOriginalFilename();
            String fileNameWithoutExtension = FilenameUtils.removeExtension(fileName);
            briefDocument.setName(fileNameWithoutExtension);
            String filExtName = FilenameUtils.getExtension(fileName);
            briefDocument.setExtName(DOT.concat(filExtName));
            briefDocument.setDate(Date.valueOf(now));
            service.create(briefDocument);
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/send",
            method = RequestMethod.GET)
    public void sendFile(@RequestParam("id") String docId,
                         HttpServletResponse response) {
        Long id = Long.valueOf(docId);
        BriefDocument briefDocument = service.findOne(id);
        String filePath = briefDocument.getPath()
                .concat(SLASH)
                .concat(briefDocument.getName())
                .concat(briefDocument.getExtName());
        File file = new File(filePath);
        mailService.send("TEST", "1805bunin.oleksandr1805@gmail.com", file);
        System.out.println();
    }

    @RequestMapping(path = "/download",
            method = RequestMethod.GET)
    public void download(@RequestParam("id") String docId,
                         HttpServletResponse response) {
        Long id = Long.valueOf(docId);
        BriefDocument briefDocument = service.findOne(id);
        String filePath = briefDocument.getPath()
                .concat(SLASH)
                .concat(briefDocument.getName())
                .concat(briefDocument.getExtName());
        File file = new File(filePath);
        sendData(response, file, briefDocument.getExtName());
    }

    private void sendData(HttpServletResponse response, File file, String ext) {
        try (InputStream in = new FileInputStream(file)) {
            String contentType = CONTENT_TYPE_MAP.getContentTypeFor(ext);
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
