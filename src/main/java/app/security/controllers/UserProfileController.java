package app.security.controllers;

import app.controllers.customtenant.JsonSupportController;
import app.security.models.auth.CustomUser;
import app.security.wrappers.ICustomUserWrapper;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
public class UserProfileController
        extends JsonSupportController {

    @Autowired
    private ICustomUserWrapper userWrapper;

    @Autowired
    private GoogleAuthenticator authenticator;

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
}
