package app.controllers;

import app.service.IBriefDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @Autowired
    private IBriefDocumentService service;

    @RequestMapping("/helloworld")
    public String handler(Model model) {
        return "helloworld";
    }
}
