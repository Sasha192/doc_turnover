package app.security.controllers;

import app.controllers.JsonSupportController;
import app.security.models.UserDto;
import app.security.service.IUserService;
import app.security.wrappers.IAuthenticationManagement;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class RegistrationController extends JsonSupportController {

    private final IUserService userService;

    private final IAuthenticationManagement authenticationManagement;

    private VerificationCodeUtil codeUtil;

    public RegistrationController(IUserService userService,
                                  IAuthenticationManagement authenticationManagement,
                                  VerificationCodeUtil codeUtil) {
        this.userService = userService;
        this.authenticationManagement = authenticationManagement;
        this.codeUtil = codeUtil;
    }

    @PostMapping(value = "/reg", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void firstStepRegistration(@Validated(UserDto.New.class)
                                      @RequestBody UserDto userDto,
                                      HttpServletRequest req,
                                      HttpServletResponse res)
            throws IOException {
        final Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication != null) {
            authenticationManagement.invalidate(req, res);
        }
        if (userService.retrieveByName(userDto.getEmail()) != null) {
            sendDefaultJson(res, false, "User with such email is already exist");
            return;
        }
        codeUtil.createVerificationCode(userDto);
        codeUtil.sendVerificationCode(userDto, req, res);
        // @TODO : Create more beautiful verification email
    }

}
