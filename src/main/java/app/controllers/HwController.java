package app.controllers;

import app.models.BriefDocument;
import app.service.IBriefDocumentService;

import java.sql.Date;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HwController {

    @Autowired
    private IBriefDocumentService service;

    @RequestMapping("/helloworld")
    public String handler(Model model) {
        BriefDocument briefDocument = new BriefDocument();
        briefDocument.setName("ss");
        briefDocument.setPath("/");
        briefDocument.setCreationDate(Date.valueOf(LocalDate.now()));
        briefDocument.setModificationDate(Date.valueOf(LocalDate.now()));
        model.addAttribute("helloWorld", new String("hello"));
        return "helloworld";
    }

}
