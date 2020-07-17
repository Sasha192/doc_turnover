package app.controllers;

import app.service.IBriefDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    @Autowired
    private IBriefDocumentService service;

    @RequestMapping("/archive")
    public String handler(final ModelMap map) {
        //map.addAttribute("model_array", service.findAll());
        return "archive";
    }

    @RequestMapping("/profile")
    public String controller2(final ModelMap map) {
        // map.addAttribute("model_array", service.findAll());
        return "profile";
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String loginPage(final HttpServletResponse response) {
        return "access";
    }
}
