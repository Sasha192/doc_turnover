package app.security.controllers;

import app.configuration.spring.constants.Constants;
import app.controllers.JsonSupportController;
import app.security.models.UserDto;
import app.security.models.auth.CustomUser;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import app.security.wrappers.IAuthenticationManagement;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthenticationController extends JsonSupportController {

    // @TODO Verification code : поскольку они хранятся в памяти. Можно ли осуществить атаку DDOS ?
    // @TODO Можно построить эскплоит на overhead main memory
    // @TODO Решение : как - только происходит overhead,

    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");

    private final IUserService userService;

    private final DefaultPasswordEncoder encoder;

    private final Constants constants;

    private final IAuthenticationManagement authenticationManagement;

    private final VerificationCodeUtil codeUtil;

    @Autowired
    public AuthenticationController(IUserService userService,
                                    DefaultPasswordEncoder encoder,
                                    Constants constants,
                                    IAuthenticationManagement authenticationManagement,
                                    VerificationCodeUtil codeUtil) {
        this.userService = userService;
        this.encoder = encoder;
        this.constants = constants;
        this.authenticationManagement = authenticationManagement;
        this.codeUtil = codeUtil;
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        authenticationManagement.invalidate(req, res);
        this.sendDefaultJson(res, true, "");
    }

    @PostMapping("/verify")
    public void verify(@RequestParam("verificationCode") String verificationCode,
                       HttpServletRequest request, HttpServletResponse res)
            throws IOException {
        UserDto dto = codeUtil.verify(request, verificationCode);
        if (dto != null) {
            authenticationManagement.authenticate(request, res, dto);
            this.sendDefaultJson(res, true, "");
            return;
        } else {
            sendDefaultJson(res, false, "Something go wrong");
            return;
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
        codeUtil.createVerificationCode(userDto);
        codeUtil.sendVerificationCode(userDto, req, res);
    }

    //@TODO IMPL!!!
    @PostMapping(value = "/login/authenticator")
    public void loginAuthenticator(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestParam("username") String username,
                                   @RequestParam("code") String authCode)
            throws IOException {
        CustomUser user = userService.retrieveByName(username);
        if (user == null) {
            this.sendDefaultJson(response, false, "Wrong Credentials");
            return;
        }
        authenticationManagement.authenticate(user, request);
    }

}
