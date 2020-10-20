package app.security.controllers;

import app.configuration.spring.constants.Constants;
import app.controllers.customtenant.JsonSupportController;
import app.customtenant.service.extapis.IMailService;
import app.customtenant.service.impl.ExecutionService;
import app.security.models.UserDto;
import app.security.models.auth.VerificationCode;
import app.security.service.IUserService;
import app.security.utils.VerificationCodeGenerator;
import app.security.utils.VerificationMailTemplater;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class VerificationCodeUtil extends JsonSupportController {

    public static final String TABLE_USER_CODE_STRING = "TABLE_USER_CODE_STRING";

    public static final long CODE_EXPIRATION_TIME = 900000L;

    private static final Map<Long, VerificationCode> verificationTable = new HashMap<>();

    private static final String USER_DTO = "USER_DTO";

    private final IUserService userService;

    private final Constants constants;

    private final VerificationMailTemplater verificationMailTemplater;

    private final ExecutionService executionService;

    private final IMailService mailService;

    public VerificationCodeUtil(IUserService userService,
                                Constants constants,
                                VerificationMailTemplater verificationMailTemplater,
                                ExecutionService executionService,
                                IMailService mailService) {
        this.userService = userService;
        this.constants = constants;
        this.verificationMailTemplater = verificationMailTemplater;
        this.executionService = executionService;
        this.mailService = mailService;
    }

    public void sendVerificationCode(final UserDto userDto,
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
        session.setAttribute(TABLE_USER_CODE_STRING, userDto.getVerificationCode());
        session.setAttribute(USER_DTO, userDto);
        sendDefaultJson(res, true, "");
    }

    public void createVerificationCode(final UserDto userDto) {
        VerificationCode code = new VerificationCode();
        code.setCode(VerificationCodeGenerator.generate());
        userDto.setVerificationCode(code);
    }

    public UserDto verify(HttpServletRequest request, String codeToCheck) {
        HttpSession session = request.getSession();
        if (session != null) {
            Object o = session.getAttribute(TABLE_USER_CODE_STRING);
            if (o != null && o.equals(codeToCheck.trim())) {
                o = session.getAttribute(USER_DTO);
                if (o != null && o instanceof UserDto) {
                    session.invalidate();
                    return (UserDto) o;
                }
            }
        }
        return null;
    }
}
