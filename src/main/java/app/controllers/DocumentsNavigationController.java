package app.controllers;

import app.models.BriefArchive;
import app.models.BriefDocument;
import app.service.IBriefDocumentService;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/doc")
public class DocumentsNavigationController extends JsonSupportController {

    private static String ROOT_PATH = "/home/kolmogorov/Java_Practice/bcrew/"
            + "doc_turnover/src/main/webapp/archive";

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
    public ResponseEntity upload(@RequestParam("file") MultipartFile file,
                                 ModelMap modelMap) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        String filePath = ROOT_PATH.concat("/" + year)
                .concat("/" + month)
                .concat("/" + day);
        File fileFolder = new File(filePath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        try {
            File fileToSave = new File(filePath.concat("/" + file.getOriginalFilename()));
            file.transferTo(fileToSave);
            BriefDocument briefDocument = new BriefDocument();
            briefDocument.setPath(filePath);
            briefDocument.setName(file.getOriginalFilename());
            briefDocument.setCreationDate(Date.valueOf(now));
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
