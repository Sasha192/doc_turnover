package app.security.controllers;

import app.configuration.spring.constants.Constants;
import app.controllers.customtenant.JsonSupportController;
import app.customtenant.service.interfaces.IPerformerService;
import app.tenantconfiguration.TenantContext;
import app.security.models.UserDto;
import app.security.service.IUserService;
import app.security.wrappers.IAuthenticationManagement;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/main/auth")
public class RegistrationController extends JsonSupportController {

    private static final Logger LOGGER = Logger.getLogger("authLogger");

    private final IUserService userService;

    private final IAuthenticationManagement authenticationManagement;

    private VerificationCodeUtil codeUtil;

    private final IPerformerService performerService;

    public RegistrationController(IUserService userService,
                                  IAuthenticationManagement authenticationManagement,
                                  VerificationCodeUtil codeUtil,
                                  IPerformerService performerService) {
        this.userService = userService;
        this.authenticationManagement = authenticationManagement;
        this.codeUtil = codeUtil;
        this.performerService = performerService;
    }

    @GetMapping(value = "/reg/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void defaultRegistration(@RequestParam("email") String email,
                                    HttpServletRequest req,
                                    HttpServletResponse res)
            throws IOException {
        if (userService.retrieveByName(email) != null) {
            sendDefaultJson(res, false, "User with such email is already exist");
            return;
        }
        UserDto userDto = new UserDto();
        userDto.setLogin(email);
        userDto.setRememberMe(true);
        codeUtil.createVerificationCode(userDto);
        codeUtil.sendVerificationCode(userDto, req, res);
        sendDefaultJson(res, true, "");
    }

    @PostMapping(value = "/reg/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void defaultRegistration(@Validated(UserDto.New.class)
                                    @RequestBody UserDto userDto,
                                    HttpServletRequest req,
                                    HttpServletResponse res)
            throws IOException {
        try {
            HttpSession session = req.getSession();
            if (session != null) {
                Object o = session.getAttribute(Constants.USER_DTO);
                if (o != null && o instanceof UserDto) {
                    UserDto oldDto = (UserDto) o;
                    userDto.setLogin(oldDto.getLogin());
                    authenticationManagement.authenticate(req, res, userDto);
                    session.removeAttribute(Constants.USER_DTO);
                    session.setAttribute(Constants.TENANT_SESSION_ID,
                            TenantContext.PHANTOM_TENANT_IDENTIFIER);
                }
            }
            sendDefaultJson(res, true, "");
        } catch (Exception e) {
            LOGGER.error(e);
            sendDefaultJson(res, false, "Помилка на сервері, будь ласка. Спробуйте пізніше");
        }
    }

}
