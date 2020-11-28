package app.security.controllers;

import app.controllers.customtenant.JsonSupportController;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.interfaces.IPerformerService;
import app.security.models.auth.CustomUser;
import app.security.models.auth.UserInfo;
import app.security.service.IUserService;
import app.security.wrappers.ICustomUserWrapper;
import app.tenantconfiguration.TenantContext;
import app.utils.IimageStorage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.docx4j.wml.P;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/com/settings")
public class UserProfileController
        extends JsonSupportController {

    private final ICustomUserWrapper userWrapper;

    private final GoogleAuthenticator authenticator;

    private final IimageStorage imageStorage;

    private final IUserService userService;

    private final PasswordEncoder encoder;

    private final IPerformerService performerService;

    @Autowired
    public UserProfileController(ICustomUserWrapper userWrapper,
                                 IimageStorage imageStorage,
                                 IUserService userService,
                                 PasswordEncoder encoder,
                                 IPerformerService performerService) {
        this.userWrapper = userWrapper;
        this.encoder = encoder;
        this.performerService = performerService;
        this.authenticator = new GoogleAuthenticator();
        this.imageStorage = imageStorage;
        this.userService = userService;
    }

    @RequestMapping(value = "/authenticator/generate")
    public void generateGAuth(HttpServletResponse response,
                              HttpServletRequest request)
            throws WriterException, IOException {
        CustomUser user = userWrapper.retrieveUser(request);
        String username = user.getLogin();
        final GoogleAuthenticatorKey key = authenticator.createCredentials(username);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String otp = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("37", username, key);
        BitMatrix bitMatrix = qrCodeWriter.encode(otp, BarcodeFormat.QR_CODE, 200, 200);
        ServletOutputStream outputStream = response.getOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        outputStream.close();
    }

    @RequestMapping(value = "/change/img", method = RequestMethod.POST)
    public void uploadImage(HttpServletResponse response,
                            HttpServletRequest request,
                            @RequestParam("file") final MultipartFile mfile)
            throws IOException {
        String prev = TenantContext.getTenant();
        TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
        CustomUser user = userWrapper.retrieveUser(request);
        UserInfo info = user.getUserInfo();
        imageStorage.upload(mfile, info);
        userService.updateUserInfo(info);
        sendDefaultJson(response, true, "");
        Set<String> tenants = user.getTenants();
        if (tenants != null && !tenants.isEmpty()) {
            for (String tenant : tenants) {
                TenantContext.setTenant(tenant);
                Performer perf = performerService.retrieveByUserId(user.getId());
                perf.setImgPath(info.getImgPath());
                performerService.update(perf);
            }
        }
        TenantContext.setTenant(prev);
    }

    @RequestMapping(value = "/change/password", method = RequestMethod.POST)
    public void changePassword(HttpServletRequest request, HttpServletResponse response,
                               @RequestBody String body)
            throws IOException {
        JsonElement jsonBody = JsonParser.parseString(body);
        if (!jsonBody.isJsonObject()) {
            sendDefaultJson(response, false,
                    "Please, give me correct Media Type.");
        }
        JsonObject o = jsonBody.getAsJsonObject();
        String newPwd = o.get("newPasswd").getAsString();
        String oldPwd = o.get("oldPasswd").getAsString();
        String prev = TenantContext.getTenant();
        TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
        CustomUser user = userWrapper.retrieveUser(request);
        if (encoder.matches(oldPwd, user.getPassword())) {
            String encodedPwd = encoder.encode(newPwd);
            user.setPassword(encodedPwd);
            userService.update(user);
            sendDefaultJson(response, true, "");
        } else {
            sendDefaultJson(response, false, "Old password wrong");
        }
        TenantContext.setTenant(prev);
    }
}
