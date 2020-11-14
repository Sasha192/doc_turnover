package app.security.controllers;

import app.controllers.customtenant.JsonSupportController;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.serialization.ExcludeStrategies;
import app.security.models.auth.CustomUser;
import app.security.models.auth.UserInfo;
import app.security.service.IUserService;
import app.security.wrappers.ICustomUserWrapper;
import app.security.wrappers.IPerformerWrapper;
import app.utils.IimageStorage;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/settings")
public class UserProfileController
        extends JsonSupportController {

    private final ICustomUserWrapper userWrapper;

    private final GoogleAuthenticator authenticator;

    private final IimageStorage imageStorage;

    private final IUserService userService;

    private final IPerformerWrapper performerWrapper;

    private final PasswordEncoder encoder;

    @Autowired
    public UserProfileController(ICustomUserWrapper userWrapper,
                                 IimageStorage imageStorage,
                                 IUserService userService,
                                 IPerformerWrapper performerWrapper,
                                 PasswordEncoder encoder) {
        this.userWrapper = userWrapper;
        this.performerWrapper = performerWrapper;
        this.encoder = encoder;
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

    @RequestMapping(value = "/change/image", method = RequestMethod.POST)
    public void uploadImage(HttpServletResponse response,
                            HttpServletRequest request,
                            @RequestParam("file") final MultipartFile mfile)
            throws IOException {
        CustomUser user = userWrapper.retrieveUser(request);
        UserInfo info = user.getUserInfo();
        imageStorage.upload(mfile, info);
        userService.updateUserInfo(info);
    }

    @RequestMapping(value = "/change/password", method = RequestMethod.POST)
    public void changePassword(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam("newPasswd") final String newPwd,
                               @RequestParam("oldPasswd") final String oldPwd)
            throws IOException {
        CustomUser user = userWrapper.retrieveUser(request);
        if (encoder.matches(oldPwd, user.getPassword())) {
            String encodedPwd = encoder.encode(newPwd);
            user.setPassword(encodedPwd);
            userService.update(user);
            sendDefaultJson(response, true, "");
        } else {
            sendDefaultJson(response, false, "Old password wrong");
        }
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public void info(HttpServletResponse response,
                     HttpServletRequest request) throws IOException {
        GsonBuilder builder = new GsonBuilder()
                .addSerializationExclusionStrategy(
                        ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
                .setPrettyPrinting();
        UserInfo info = userWrapper.retrieveUser(request)
                .getUserInfo();
        JsonObject jInfo = builder.create()
                .toJsonTree(info)
                .getAsJsonObject();
        Performer performer = performerWrapper.retrievePerformer(request);
        if (performer != null) {
            JsonObject jPerformer = builder.create()
                    .toJsonTree(performer)
                    .getAsJsonObject();
            jInfo.add("performer", jPerformer);
        }
        sendDefaultJson(response, jInfo);
    }
}
