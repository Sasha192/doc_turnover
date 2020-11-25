package app.controllers.defaultcontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ViewsController {

    @RequestMapping
    public String root() {
        return "index";
    }

    @RequestMapping("/com")
    public String com() {
        return "index";
    }

    @RequestMapping("/com/boards")
    public String boards() {
        return "index";
    }

    @RequestMapping("/com/files")
    public String files() {
        return "index";
    }

    @RequestMapping("/com/clPanel")
    public String clPanel() {
        return "index";
    }

    @RequestMapping("/main")
    public String main() {
        return "main";
    }

    @RequestMapping("/main/signup")
    public String signUp() {
        return "signup";
    }

    @RequestMapping("/main/confirm")
    public String confirmCode() {
        return "confirm";
    }

    @RequestMapping("/main/econfig")
    public String econfig() {
        return "index";
    }

    @RequestMapping("/com/uconfig")
    public String userConfig() {
        return "index";
    }

    @RequestMapping("/com/charts")
    public String charts() {
        return "index";
    }

    @RequestMapping("/main/signin")
    public String mainPage() {
        return "signin";
    }
}
