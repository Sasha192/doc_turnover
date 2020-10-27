package app.security.controllers;

import app.controllers.customtenant.JsonSupportController;
import app.security.models.auth.CustomUser;
import app.security.wrappers.IAuthenticationManagement;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth")
public class OAuthController extends JsonSupportController {

    private final IAuthenticationManagement authenticationManagement;

    private GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
            .Builder(new NetHttpTransport(), new JacksonFactory())
            .build();

    @Autowired
    public OAuthController(IAuthenticationManagement authenticationManagement) {
        this.authenticationManagement = authenticationManagement;
    }

    @PostMapping(value = "/google")
    public void googleAuth(@RequestBody String tokenId,
                           HttpServletRequest req,
                           HttpServletResponse res)
            throws IOException {
        try {
            GoogleIdToken idToken = verifier.verify(tokenId);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                CustomUser user = retrieveUser(payload);
                if (user == null) {
                    authenticationManagement.authenticate(user, req);
                    sendDefaultJson(res, true, "");
                } else {
                    sendDefaultJson(res, false, "Oops, my bad :(");
                }
            }
        } catch (GeneralSecurityException e) {
            getExceptionLogger().error(RegistrationController.class.getCanonicalName());
            getExceptionLogger().error(e);
        }
    }

    private CustomUser retrieveUser(GoogleIdToken.Payload payload) {
        boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
        if (emailVerified) {
            CustomUser user = new CustomUser(payload);
            return user;
        } else {
            return null;
        }
    }

}
