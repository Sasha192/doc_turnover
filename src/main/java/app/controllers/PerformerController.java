package app.controllers;

import app.models.basic.CustomUser;
import app.models.basic.Performer;
import app.models.serialization.ExcludeStrategies;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import app.security.wrappers.AuthenticationWrapper;
import app.security.wrappers.PerformerWrapper;
import app.service.interfaces.IPerformerService;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/performer")
public class PerformerController extends JsonSupportController {

    // @TODO : What should we do if connection is not via http ??? Could it be ???
    // @TODO : any operations to DB perform in queue???

    private IUserService userService;
    private AuthenticationWrapper authenticationWrapper;
    private final PerformerWrapper performerWrapper;
    private IPerformerService performerService;
    private DefaultPasswordEncoder encoder;

    @Autowired
    public PerformerController(IUserService userService,
                               AuthenticationWrapper authenticationWrapper,
                               IPerformerService performerService,
                               DefaultPasswordEncoder encoder, PerformerWrapper performerWrapper) {
        this.userService = userService;
        this.authenticationWrapper = authenticationWrapper;
        this.performerService = performerService;
        this.encoder = encoder;
        this.performerWrapper = performerWrapper;
    }

    @RequestMapping("/my/info")
    public void myInfo(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        GsonBuilder builder = new GsonBuilder()
                .addSerializationExclusionStrategy(
                        ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
                .setPrettyPrinting();
        writeToResponse(response, builder, performer);
    }

    @RequestMapping("my/password")
    public void changeMyPassword(@RequestParam(value = "new_password", required = false)
                                         String newPassword,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        if (!newPassword.matches(".{8,}")) {
            sendDefaultJson(response, false, "Weak password!");
            return;
        }
        Principal principal = (Principal) authenticationWrapper.getPrincipal(request);
        if (principal != null) {
            CustomUser user = userService.retrieveByName(principal.getName());
            newPassword = encoder.encode(newPassword);
            user.setPassword(newPassword);
            userService.update(user);
        }
        sendDefaultJson(response, true, "");
    }
}
