package app.security.controllers;

import app.controllers.customtenant.JsonSupportController;
import app.security.models.UserDto;
import app.security.wrappers.IAuthenticationManagement;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/main/auth/verify")
public class VerificationCodeController extends JsonSupportController {

    // @TODO Verification code : поскольку они хранятся в памяти. Можно ли осуществить атаку DDOS ?
    // @TODO Можно построить эскплоит на overhead main memory
    // @TODO Решение : как - только происходит overhead,

    private final IAuthenticationManagement authenticationManagement;

    private final VerificationCodeUtil codeUtil;

    public VerificationCodeController(IAuthenticationManagement authenticationManagement,
                                      VerificationCodeUtil codeUtil) {
        this.authenticationManagement = authenticationManagement;
        this.codeUtil = codeUtil;
    }

    @RequestMapping
    public void verify(@RequestParam("verificationCode") String verificationCode,
                       HttpServletRequest request, HttpServletResponse res)
            throws IOException {
        UserDto dto = codeUtil.verify(request, verificationCode);
        if (dto != null) {
            if (dto.isLoginOperation()) {
                authenticationManagement.authenticate(request, res, dto);
            }
            sendDefaultJson(res, true, "");
        } else {
            sendDefaultJson(res, false, "Невірний код");
        }
    }

}
