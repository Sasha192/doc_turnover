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
    public AuthenticationController(IUserService userService,
                                    DefaultPasswordEncoder encoder,
                                    VerificationMailTemplater templater,
                                    IMailService mailService,
                                    ExecutionService executionService) {
        this.userService = userService;
        this.encoder = encoder;
        verificationMailTemplater = templater;
        this.mailService = mailService;
        this.executionService = executionService;
    }

    @RequestMapping("/")
    public String authView() {
        return "access";
    }

    @RequestMapping("/logout")
    public void logout(final HttpServletRequest req, final HttpServletResponse res)
            throws IOException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        AuthenticationController.removeCookies(req, res);
        logoutHandler.logout(req, res, authentication);
        res.sendRedirect("/auth");
    }

    private static void removeCookies(final HttpServletRequest req, final HttpServletResponse res) {
        final Cookie[] cookies = req.getCookies();
        for (final Cookie cookie : cookies) {
            cookie.setValue(null);
            cookie.setMaxAge(0);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            res.addCookie(cookie);
        }
    }

    @PostMapping("/verify")
    public void verify(@RequestParam("verificationCode") final String verificationCode,
                       final HttpServletRequest request, final HttpServletResponse res)
            throws IOException {
        final HttpSession session = request.getSession();
        if (session == null) {
            this.sendDefaultJson(res, false, "Session expired");
        }
        final Object o = session.getAttribute(AuthenticationController.TABLE_USER);
        if (o == null) {
            this.sendDefaultJson(res, false, "Session expired. Try again");
        }
        if (!(o instanceof UserDto)) {
            AuthenticationController.LOGGER.debug("SHIT! session.getAttribute(TABLE_USER) in "
                    + AuthenticationController.class
                    + " get NOT UserDto");
        }
        {
            final UserDto dto = (UserDto) o;
            if (!dto.getVerificationCode().equals(verificationCode)) {
                this.sendDefaultJson(res, false, "Wrong verification code");
            }
            final VerificationCode code = this.retrieveVerificationCode(this.getVerificationKey(dto));
            // defendinf from csrd
            if (code != null) {
                if (AuthenticationController.EXPIRATION_TIME < System.currentTimeMillis() - code.getCreationtime()) {
                    AuthenticationController.verificationTable.remove(this.getVerificationKey(dto));
                    this.sendDefaultJson(res, false,
                            "Verification code expired. Please perform new verification code");
                }
                session.invalidate();
                final CustomUser customUser = new CustomUser(dto, this.encoder);
                customUser.setEnabled(true);
                this.userService.create(customUser);
                AuthenticationController.verificationTable.remove(this.getVerificationKey(dto));
                this.removeVerificationCode(this.getVerificationKey(dto));
                this.auth(res, request, dto);
            } else {
                this.sendDefaultJson(res, false, "You do not pass verification");
            }
        }
    }

    private void removeVerificationCode(final String verificationKey) {
        final long id = verificationKey.hashCode();
        final VerificationCode code = AuthenticationController.verificationTable.remove(id);
        if (code == null) {
            this.userService.removeVerificationCode(id);
        }
    }

    private VerificationCode retrieveVerificationCode(final String verificationKey) {
        final long id = verificationKey.hashCode();
        VerificationCode code = AuthenticationController.verificationTable.get(id);
        if (code == null) {
            code = this.userService.retrieveVerificationCode(id);
        }
        return code;
    }

    @PostMapping("/register")
    public void firstStepRegistration(@Validated(UserDto.New.class)
                                      @RequestBody final UserDto userDto,
                                      final HttpServletRequest req,
                                      final HttpServletResponse res) {
        if (this.userService.retrieveByName(userDto.getEmail()) != null) {
            this.sendDefaultJson(res, false, "User with such email is already exist");
        }
        final VerificationCode code = new VerificationCode();
        code.setCode(VerificationCodeUtil.generate());
        code.setId(this.getVerificationKey(userDto));
        this.registerVerificationCode(code);
        final String html = userDto.getVerificationCode();
        this.executionService.pushTask(new ExecutionService
                .MailSender(this.mailService, userDto.getEmail(), html));
        final HttpSession session = req.getSession(true);
        session.setAttribute(AuthenticationController.TABLE_USER, userDto);
        this.sendDefaultJson(res, false, "success");
        // @TODO : verificationMailTemplater.render(user.getVerificationCode());
        // @TODO : add VerificationCode if table.size() < 100 othrewise -> to special db_tableb
        // @TODO : checking for expired codes -> and removing then
    }

    private void registerVerificationCode(final VerificationCode code) {
        if (AuthenticationController.verificationTable.size() < 100) {
            AuthenticationController.verificationTable.put(code.getId(), code);
        } else {
            this.cleanVerificationTable();
            this.userService.registerVerificationCode(code);
        }
    }

    private void cleanVerificationTable() {
        for (final Map.Entry<Long, VerificationCode> entry : AuthenticationController.verificationTable.entrySet()) {
            VerificationCode code = null;
            if ((code = entry.getValue()) != null) {
                if (AuthenticationController.EXPIRATION_TIME > System.currentTimeMillis() - code.getCreationtime()) {
                    AuthenticationController.verificationTable.remove(entry.getKey());
                }
            }
        }
    }

    @PostMapping(value = "auth", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void auth(@Validated(UserDto.Auth.class)
                     @RequestBody final UserDto userDto,
                     final HttpServletResponse res,
                     final HttpServletRequest req)
            throws IOException {
        this.auth(res, req, userDto);
    }

    private void auth(final HttpServletResponse res,
                      final HttpServletRequest request,
                      final UserDto dto) throws IOException {
        final UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(
                dto.getEmail(), dto.getPassword());
        final Authentication auth = this.authManager.authenticate(authReq);
        final SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        final HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                sc);
        res.sendRedirect("/teamboard");
        this.sendDefaultJson(res, true, "");
    }

    private String getVerificationKey(final UserDto userDto) {
        return userDto.getVerificationCode().concat(userDto.getEmail());
    }
}
