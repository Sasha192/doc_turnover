package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewsController {

    @RequestMapping("/archive")
    public String archiveMap(ModelMap map) {
        return "archive";
    }

    @RequestMapping("/myboard")
    public String profileMap(ModelMap map) {
        return "myboard";
    }

    @RequestMapping("/auth")
    public String authMap(ModelMap map) {
        return "auth";
    }

    @RequestMapping("/teamboard")
    public String teamboardMap(ModelMap map) {
        return "tmboard";
    }

    @RequestMapping("/CLpanel")
    public String clPanel(ModelMap map) {
        return "cl_panel";
    }
}
