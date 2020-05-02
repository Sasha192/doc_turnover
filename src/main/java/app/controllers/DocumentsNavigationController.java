package app.controllers;

import app.models.BriefDocument;
import app.models.serialization.ExcludeStrategies;
import app.service.IBriefDocumentService;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
                .setExclusionStrategies(ExcludeStrategies.EXCLUDE_BDOCS, ExcludeStrategies.ONE_TO_MANY)
                .setPrettyPrinting();
        writeToResponse(response, builder, list);
    }

}
