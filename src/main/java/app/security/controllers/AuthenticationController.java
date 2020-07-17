package app.security.controllers;

import app.controllers.JsonSupportController;
import app.models.CustomUser;
import app.models.VerificationCode;
import app.security.models.UserDto;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import app.security.utils.VerificationCodeUtil;
import app.security.utils.VerificationMailTemplater;
import app.service.extapis.IMailService;
import app.service.impl.ExecutionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthenticationController extends JsonSupportController {

    // @TODO Verification code : поскольку они хранятся в памяти. Можно ли осуществить атаку DDOS ?
    // @TODO Можно построить эскплоит на overhead main memory
    // @TODO Решение : как - только происходит overhead, например больше 100 элементов, то убирать буфер и юзать DB.

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

    @RequestMapping("/")
    public String authView() {
        return "access";
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
                if (AuthenticationController.EXPIRATION_TIME < System.currentTimeMillis() - code.getCreationtime()) {
                    this.cleanData(request, res);
                    AuthenticationController.verificationTable.remove(getVerificationKey(dto));
                    sendDefaultJson(res, false,
                            "Verification code expired. Please perform new verification code");
                }
                CustomUser customUser = new CustomUser(dto, encoder);
                customUser.setEnabled(true);
                userService.create(customUser);
                removeVerificationCode(getVerificationKey(dto));
                auth(res, request, dto);
            } else {
                sendDefaultJson(res, false, "You do not pass verification");
                return;
            }
        } else {
            AuthenticationController.LOGGER.debug("SHIT! session.getAttribute(TABLE_USER) in "
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

    @PostMapping("/reg")
    public void firstStepRegistration(@Validated(UserDto.New.class)
                                      @RequestBody UserDto userDto,
                                      HttpServletRequest req,
                                      HttpServletResponse res)
            throws IOException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            this.cleanData(req, res);
        }
        if (userService.retrieveByName(userDto.getEmail()) != null) {
            sendDefaultJson(res, false, "User with such email is already exist");
        }
        this.createVerificationCode(userDto);
        this.sendVerificationCode(userDto, req, res);
        // @TODO : verificationMailTemplater.render(user.getVerificationCode());
    }

    private void sendVerificationCode(final UserDto userDto, final HttpServletRequest req, final HttpServletResponse res) {
        String html = userDto.getVerificationCode();
        executionService.pushTask(new ExecutionService
                .MailSender(mailService, userDto.getEmail(), html));
        HttpSession session = req.getSession(true);
        session.setAttribute(AuthenticationController.TABLE_USER, userDto);
        sendDefaultJson(res, true, "success");
    }

    private void createVerificationCode(final UserDto userDto) {
        VerificationCode code = new VerificationCode();
        code.setCode(VerificationCodeUtil.generate());
        code.setId(getVerificationKey(userDto));
        registerVerificationCode(code);
        userDto.setVerificationCode(code);
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
                if (AuthenticationController.EXPIRATION_TIME > System.currentTimeMillis() - code.getCreationtime()) {
                    AuthenticationController.verificationTable.remove(entry.getKey());
                }
            }
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void authFirstStep(@Validated(UserDto.Auth.class)
                              @RequestBody UserDto userDto,
                              HttpServletResponse res,
                              HttpServletRequest req) {
        this.createVerificationCode(userDto);
        this.sendVerificationCode(userDto, req, res);
    }

    private void auth(HttpServletResponse res,
                      HttpServletRequest request,
                      UserDto dto) throws IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            this.cleanData(request, res);
        }
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(
                dto.getEmail(), dto.getPassword());
        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                sc);
        res.sendRedirect("/archive");
    }

    private String getVerificationKey(UserDto userDto) {
        return userDto.getVerificationCode().concat(userDto.getEmail());
    }
}
