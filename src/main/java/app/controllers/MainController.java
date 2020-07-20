package app.controllers;

import app.service.IBriefDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @Autowired
    private IBriefDocumentService service;

    @RequestMapping("/archive")
    public String archiveMap(ModelMap map) {
        return "archive";
    }

    @RequestMapping("/profile")
    public String profileMap(ModelMap map) {
        return "profile";
    }

    @RequestMapping("/auth")
    public String authMap(ModelMap map) {
        return "auth";
    }
}
