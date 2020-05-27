package app.controllers;

import app.controllers.responses.ResponseJsonText;
import app.models.BriefArchive;
import app.models.BriefDocument;
import app.models.serialization.ExcludeStrategies;
import app.service.IBriefDocumentService;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseJsonText upload(@RequestParam("file") MultipartFile file,
                                   ModelMap modelMap) {
        System.out.println();
        return new ResponseJsonText(Boolean.TRUE, "Everything is Alright");
    }
}
