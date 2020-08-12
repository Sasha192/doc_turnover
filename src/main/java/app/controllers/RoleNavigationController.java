package app.controllers;

import app.security.models.SimpleRole;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
