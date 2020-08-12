package app.controllers;

import app.security.models.SimpleRole;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/roles")
public class RoleNavigationController extends JsonSupportController {

    @RequestMapping("/list")
    public void getRoles(HttpServletResponse response)
            throws IOException {
        List<SimpleRole> roles = new ArrayList<>(Arrays.asList(SimpleRole.values()));
        sendDefaultJson(response, roles);
    }

}
