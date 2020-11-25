package app.security.controllers;

import app.configuration.spring.constants.Constants;
import app.controllers.customtenant.JsonSupportController;
import app.security.models.UserDto;
import app.security.models.auth.CustomUser;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import app.security.wrappers.IAuthenticationManagement;
import app.security.wrappers.IAuthenticationViaCookies;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/main/auth")
public class LoginController extends JsonSupportController {

    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");

    private final IUserService userService;

    private final DefaultPasswordEncoder encoder;

    private final Constants constants;

    private final IAuthenticationManagement authenticationManagement;

    private final VerificationCodeUtil codeUtil;

    private final IAuthenticationViaCookies authenticationViaCookies;

    private final GoogleAuthenticator authenticator;

    @Autowired
    public LoginController(IUserService userService,
                           DefaultPasswordEncoder encoder,
                           Constants constants,
                           IAuthenticationManagement authenticationManagement,
                           VerificationCodeUtil codeUtil,
                           IAuthenticationViaCookies authenticationViaCookies) {
        this.userService = userService;
        this.encoder = encoder;
        this.constants = constants;
        this.authenticationManagement = authenticationManagement;
        this.codeUtil = codeUtil;
        this.authenticationViaCookies = authenticationViaCookies;
        this.authenticator = new GoogleAuthenticator();
    }

    @PostMapping(value = "/login")
    public void tryAuthViaCookies(HttpServletRequest request,
                                  HttpServletResponse response)
            throws IOException, ServletException {
        CustomUser user = authenticationViaCookies
                .authenticatedViaCookies(request);
        if (user != null) {
            sendDefaultJson(response, true, "");
        } else {
            request.getRequestDispatcher("/main/auth/login/default")
                    .forward(request, response);
        }
    }

    @PostMapping(value = "/login/default", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void authFirstStep(@Validated(UserDto.Auth.class)
                              @RequestBody UserDto userDto,
                              HttpServletResponse res,
                              HttpServletRequest req)
            throws IOException {
        CustomUser user = userService.retrieveByName(userDto.getLogin());
        if (user == null) {
            this.sendDefaultJson(res, false, "Wrong credentials!");
            return;
        }
        if (!encoder.matches(userDto.getPassword(), user.getPassword())) {
            this.sendDefaultJson(res, false, "Wrong Credentials");
            return;
        }
        userDto.setLoginOperation(true);
        codeUtil.createVerificationCode(userDto);
        codeUtil.sendVerificationCode(userDto, req, res);
        sendDefaultJson(res, true, "");
    }

    @PostMapping(value = "/login/authenticator")
    public void loginAuthenticator(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestParam("username") String username,
                                   @RequestParam("code") String authCode)
            throws IOException {
        try {
            int code = Integer.valueOf(authCode).intValue();
            if (authenticator.authorizeUser(username, code)) {
                CustomUser user = userService.retrieveByName(username);
                authenticationManagement.authenticate(user, request);
                sendDefaultJson(response, true, "");
            }
        } catch (NumberFormatException ex) {
            sendDefaultJson(response, false, "Wrong Authentication Code");
        }
    }

}
