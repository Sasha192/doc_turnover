package app.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/block")
public class BlockingController {

    @RequestMapping("/entry")
    public String entry(HttpServletRequest request,
                      HttpServletResponse response) {
        return "auth";
    }

}
