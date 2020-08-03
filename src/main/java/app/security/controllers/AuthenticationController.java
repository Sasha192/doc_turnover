package app.security.controllers;

import app.configuration.spring.constants.Constants;
import app.controllers.JsonSupportController;
import app.models.basic.CustomUser;
import app.models.VerificationCode;
import app.security.models.UserDto;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import app.security.utils.VerificationCodeUtil;
import app.security.utils.VerificationMailTemplater;
import app.service.extapis.IMailService;
import app.service.impl.ExecutionService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
    private static final long EXPIRATION_TIME = 900000L;

    private final IUserService userService;

    private final DefaultPasswordEncoder encoder;

    private final VerificationMailTemplater verificationMailTemplater;

    private final IMailService mailService;

    private final ExecutionService executionService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    @Qualifier("app_constants")
    private Constants constants;

    @Autowired
    public AuthenticationController(final IUserService userService,
                                    final DefaultPasswordEncoder encoder,
                                    final VerificationMailTemplater templater,
                                    final IMailService mailService,
                                    final ExecutionService executionService) {
        this.userService = userService;
        this.encoder = encoder;
        this.verificationMailTemplater = templater;
        this.mailService = mailService;
        this.executionService = executionService;
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        this.cleanData(req, res);
        res.sendRedirect("/auth");
    }

    private void cleanData(final HttpServletRequest req, final HttpServletResponse res) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context == null ? null :
                context.getAuthentication();
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        AuthenticationController.removeCookies(req, res);
        logoutHandler.logout(req, res, authentication);
    }

    private static void removeCookies(HttpServletRequest req, HttpServletResponse res) {
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setValue(null);
            cookie.setMaxAge(0);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            res.addCookie(cookie);
        }
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
                if (EXPIRATION_TIME
                        < System.currentTimeMillis() - code.getCreationtime()) {
                    this.cleanData(request, res);
                    verificationTable.remove(getVerificationKey(dto));
                    sendDefaultJson(res, false,
                            "Verification code expired. Please perform new verification code");
                }
                if (userService.retrieveByName(dto.getEmail()) == null) {
                    CustomUser customUser = new CustomUser(dto, encoder);
                    customUser.setEnabled(true);
                    userService.create(customUser);
                }
                removeVerificationCode(getVerificationKey(dto));
                auth(res, request, dto);
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
            this.cleanData(req, res);
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
                if (EXPIRATION_TIME > System.currentTimeMillis() - code.getCreationtime()) {
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

    public void auth(HttpServletResponse res,
                      HttpServletRequest request,
                      UserDto dto) throws IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            this.cleanData(request, res);
        }
        session = request.getSession(true);
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(
                dto.getEmail(), dto.getPassword());
        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                sc);
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
