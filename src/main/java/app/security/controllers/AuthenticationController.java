package app.security.controllers;

import app.controllers.JsonSupportController;
import app.models.CustomUser;
import app.models.VerificationCode;
import app.security.models.UserDto;
import app.security.service.IUserService;
import app.security.service.impl.UserService;
import app.security.utils.DefaultPasswordEncoder;
import app.security.utils.VerificationCodeUtil;
import app.security.utils.VerificationMailTemplater;
import app.service.extapis.IMailService;
import app.service.impl.ExecutionService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class AuthenticationController extends JsonSupportController {

    // @TODO Verification code : поскольку они хранятся в памяти. Можно ли осуществить атаку DDOS ?
    // @TODO Можно построить эскплоит на overhead main memory
    // @TODO Решение : как - только происходит overhead, например больше 100 элементов, то убирать буфер и юзать DB.

    private static final Map<Long, VerificationCode> verificationTable = new HashMap<>();
    private static final String TABLE_USER = "TABLE_USER";
    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");
    private static final long EXPIRATION_TIME = 900000L;

    private IUserService userService;

    private DefaultPasswordEncoder encoder;

    private VerificationMailTemplater verificationMailTemplater;

    private IMailService mailService;

    private ExecutionService executionService;

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

    @PostMapping(value = "/verify")
    public void verify(@RequestParam("verificationCode") String verificationCode,
                       HttpServletRequest request, HttpServletResponse res)
            throws IOException {
        HttpSession session = request.getSession();
        if (session == null) {
            sendDefaultJson(res, false, "Session expired");
        }
        Object o = session.getAttribute(TABLE_USER);
        if (o == null) {
            sendDefaultJson(res, false, "Session expired. Try again");
        }
        if (!(o instanceof UserDto)) {
            LOGGER.debug("SHIT! session.getAttribute(TABLE_USER) in "
                    + AuthenticationController.class
                    + " get NOT UserDto");
        }
        {
            UserDto dto = (UserDto) o;
            if (!dto.getVerificationCode().equals(verificationCode)) {
                sendDefaultJson(res, false, "Wrong verification code");
            }
            VerificationCode code = retrieveVerificationCode(getVerificationKey(dto));
            // defendinf from csrd
            if (code != null) {
                if (EXPIRATION_TIME < System.currentTimeMillis() - code.getCreationtime()) {
                    verificationTable.remove(getVerificationKey(dto));
                    sendDefaultJson(res, false,
                            "Verification code expired. Please perform new verification code");
                }
                session.invalidate();
                CustomUser customUser = new CustomUser(dto, encoder);
                customUser.setEnabled(true);
                userService.create(customUser);
                verificationTable.remove(getVerificationKey(dto));
                removeVerificationCode(getVerificationKey(dto));
                auth(res, request, dto);
            } else {
                sendDefaultJson(res, false, "You do not pass verification");
            }
        }
    }

    private void removeVerificationCode(String verificationKey) {
        long id = verificationKey.hashCode();
        VerificationCode code = verificationTable.remove(id);
        if (code == null) {
            userService.removeVerificationCode(id);
        }
    }

    private VerificationCode retrieveVerificationCode(String verificationKey) {
        long id = verificationKey.hashCode();
        VerificationCode code = verificationTable.get(id);
        if (code == null) {
            code = userService.retrieveVerificationCode(id);
        }
        return code;
    }

    @PostMapping(value = "/register")
    public void firstStepRegistration(@Validated(UserDto.New.class)
                                      @RequestBody UserDto userDto,
                                      HttpServletRequest req,
                                      HttpServletResponse res) {
        if (userService.retrieveByName(userDto.getEmail()) != null) {
            sendDefaultJson(res, false, "User with such email is already exist");
        }
        VerificationCode code = new VerificationCode();
        code.setCode(VerificationCodeUtil.generate());
        code.setId(getVerificationKey(userDto));
        registerVerificationCode(code);
        String html = userDto.getVerificationCode();
        executionService.pushTask(new ExecutionService
                .MailSender(mailService, userDto.getEmail(), html));
        HttpSession session = req.getSession(true);
        session.setAttribute(TABLE_USER, userDto);
        sendDefaultJson(res, false, "success");
        // @TODO : verificationMailTemplater.render(user.getVerificationCode());
        // @TODO : add VerificationCode if table.size() < 100 othrewise -> to special db_tableb
        // @TODO : checking for expired codes -> and removing then
    }

    private void registerVerificationCode(VerificationCode code) {
        if (verificationTable.size() < 100) {
            verificationTable.put(code.getId(), code);
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

    @PostMapping(value = "auth", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void auth(@Validated(UserDto.Auth.class)
                     @RequestBody UserDto userDto,
                     HttpServletResponse res,
                     HttpServletRequest req)
            throws IOException {
        auth(res, req, userDto);
    }

    private void auth(HttpServletResponse res,
                      HttpServletRequest request,
                      UserDto dto) throws IOException {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(
                dto.getEmail(), dto.getPassword());
        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                sc);
        res.sendRedirect("/teamboard");
        sendDefaultJson(res, true, "");
    }

    private String getVerificationKey(UserDto userDto) {
        return userDto.getVerificationCode().concat(userDto.getEmail());
    }
}
