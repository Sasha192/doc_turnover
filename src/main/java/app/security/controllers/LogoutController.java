package app.security.controllers;

import app.controllers.customtenant.JsonSupportController;
import app.security.wrappers.IAuthenticationManagement;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/logout")
public class LogoutController
        extends JsonSupportController {

    private final IAuthenticationManagement authenticationManagement;

    public LogoutController(IAuthenticationManagement authenticationManagement) {
        this.authenticationManagement = authenticationManagement;
    }

    @RequestMapping
    public void logout(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        authenticationManagement.invalidate(req, res);
        this.sendDefaultJson(res, true, "");
    }

}
