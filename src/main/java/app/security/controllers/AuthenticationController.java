package app.security.controllers;

import app.configuration.spring.constants.Constants;
import app.controllers.JsonSupportController;
import app.models.VerificationCode;
import app.models.basic.CustomUser;
import app.security.models.UserDto;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import app.security.utils.VerificationCodeUtil;
import app.security.utils.VerificationMailTemplater;
import app.security.wrappers.IAuthenticationManagement;
import app.service.extapis.IMailService;
import app.service.impl.ExecutionService;
import app.service.interfaces.IPerformerService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private static final Map<Long, VerificationCode> verificationTable = new HashMap<>();
    private static final String TABLE_USER = "TABLE_USER";
    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");
    private static final long CODE_EXPIRATION_TIME = 900000L;

    private final IUserService userService;

    private final DefaultPasswordEncoder encoder;

    private final VerificationMailTemplater verificationMailTemplater;

    private final IMailService mailService;

    private final ExecutionService executionService;

    private final Constants constants;

    private final IAuthenticationManagement authenticationManagement;

    @Autowired
    public AuthenticationController(final IUserService userService,
                                    final DefaultPasswordEncoder encoder,
                                    final VerificationMailTemplater templater,
                                    final IMailService mailService,
                                    final ExecutionService executionService,
                                    IPerformerService performerService,
                                    AuthenticationManager authManager,
                                    @Qualifier("app_constants") Constants constants,
                                    IAuthenticationManagement authenticationManagement) {
        this.userService = userService;
        this.encoder = encoder;
        this.verificationMailTemplater = templater;
        this.mailService = mailService;
        this.executionService = executionService;
        this.constants = constants;
        this.authenticationManagement = authenticationManagement;
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
        final HttpSession session = request.getSession();
        final UserDto dto = this.retrieveUserDto(session, res, request);
        if (dto != null) {
            if (!dto.getVerificationCode().equals(verificationCode)) {
                sendDefaultJson(res, false, "Wrong verification code");
                return;
            }
            VerificationCode code = retrieveVerificationCode(this.getVerificationKey(dto));
            if (code != null) {
                if (CODE_EXPIRATION_TIME
                        < System.currentTimeMillis() - code.getCreationtime()) {
                    authenticationManagement.invalidate(request, res);
                    verificationTable.remove(getVerificationKey(dto));
                    sendDefaultJson(res, false,
                            "Verification code expired. Please perform new verification code");
                }
                authenticationManagement.authenticate(request, res, dto);
                removeVerificationCode(getVerificationKey(dto));
                this.sendDefaultJson(res, true, "");
                return;
            } else {
                sendDefaultJson(res, false, "You do not pass verification");
                return;
            }
        } else {
            LOGGER.debug("SHIT! session.getAttribute(TABLE_USER) in "
                    + AuthenticationController.class
                    + " get NOT UserDto OR Something go wrong");
            sendDefaultJson(res, false, "Something go wrong");
            return;
        }
    }

    private void removeVerificationCode(String verificationKey) {
        long id = verificationKey.hashCode();
        VerificationCode code = AuthenticationController.verificationTable.remove(id);
        if (code == null) {
            userService.removeVerificationCode(id);
        }
    }

    private VerificationCode retrieveVerificationCode(String verificationKey) {
        long id = verificationKey.hashCode();
        VerificationCode code = AuthenticationController.verificationTable.get(id);
        if (code == null) {
            code = userService.retrieveVerificationCode(id);
        }
        return code;
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
        this.createVerificationCode(userDto);
        this.sendVerificationCode(userDto, req, res);
        // @TODO : Create more beautiful verification email
    }

    private void sendVerificationCode(final UserDto userDto,
                                      final HttpServletRequest req,
                                      final HttpServletResponse res)
            throws IOException {
        String html = userDto.getVerificationCode();
        html = verificationMailTemplater.render(html);
        String attachment = constants.get("email_template_path").getStringValue();
        FileSystemResource file = new FileSystemResource(attachment);
        executionService.pushTask(new ExecutionService
                .MailSender(mailService, userDto.getEmail(), html, file));
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        session = req.getSession(true);
        session.setAttribute(TABLE_USER, userDto);
        sendDefaultJson(res, true, "");
    }

    private void createVerificationCode(final UserDto userDto) {
        VerificationCode code = new VerificationCode();
        code.setCode(VerificationCodeUtil.generate());
        code.setId(getVerificationKey(code, userDto));
        userDto.setVerificationCode(code);
        registerVerificationCode(code);
    }

    private UserDto retrieveUserDto(final HttpSession session,
                                    final HttpServletResponse res,
                                    final HttpServletRequest req)
            throws IOException {
        if (session == null) {
            sendDefaultJson(res, false, "Session expired");
            return null;
        }
        Object o = session.getAttribute(AuthenticationController.TABLE_USER);
        if (o == null) {
            this.logout(req, res);
            sendDefaultJson(res, false, "Session expired. Try again");
            return null;
        }
        if (o instanceof UserDto) {
            return (UserDto) o;
        }
        return null;
    }

    private void registerVerificationCode(VerificationCode code) {
        // @TODO : Attack on Denial of Service;
        // @TODO : Attack on collision ???
        if (AuthenticationController.verificationTable.size() < 100) {
            AuthenticationController.verificationTable.put(code.getId(), code);
        } else {
            cleanVerificationTable();
            userService.registerVerificationCode(code);
        }
    }

    private void cleanVerificationTable() {
        for (Map.Entry<Long, VerificationCode> entry : verificationTable.entrySet()) {
            VerificationCode code = null;
            if ((code = entry.getValue()) != null) {
                if (CODE_EXPIRATION_TIME > System.currentTimeMillis() - code.getCreationtime()) {
                    verificationTable.remove(entry.getKey());
                }
            }
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void authFirstStep(@Validated(UserDto.Auth.class)
                              @RequestBody UserDto userDto,
                              HttpServletResponse res,
                              HttpServletRequest req)
            throws IOException {
        CustomUser user = userService.retrieveByName(userDto.getEmail());
        if (user == null) {
            this.sendDefaultJson(res, false, "Wrong credentials!");
            return;
        }
        if (!encoder.matches(userDto.getPassword(), user.getPassword())) {
            this.sendDefaultJson(res, false, "Wrong Credentials");
            return;
        }
        this.createVerificationCode(userDto);
        this.sendVerificationCode(userDto, req, res);
    }

    private String getVerificationKey(VerificationCode code, UserDto userDto) {
        return code.getCode().concat(userDto.getEmail());
    }

    private String getVerificationKey(UserDto userDto) {
        if (userDto.getVerificationCode() != null) {
            return userDto.getVerificationCode().concat(userDto.getEmail());
        }
        return null;
    }
}
