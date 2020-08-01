package app.controllers;

import app.models.CustomUser;
import app.models.Performer;
import app.models.serialization.ExcludeStrategies;
import app.security.controllers.AuthenticationWrapper;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import app.service.IPerformerService;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class PerformerController extends JsonSupportController {

    // @TODO : we can create class wrapper : that will hold in htppsession and retrieve info any info about user
    // @TODO : What should we do if connection is not via http ??? Could it be ???
    // @TODO : any operations to DB perform in queue???

    private IUserService userService;
    private AuthenticationWrapper authenticationWrapper;
    private IPerformerService performerService;
    private DefaultPasswordEncoder encoder;

    @Autowired
    public PerformerController(IUserService userService,
                               AuthenticationWrapper authenticationWrapper,
                               IPerformerService performerService,
                               DefaultPasswordEncoder encoder) {
        this.userService = userService;
        this.authenticationWrapper = authenticationWrapper;
        this.performerService = performerService;
        this.encoder = encoder;
    }

    @RequestMapping("/my/info")
    public void myInfo(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException {
        Principal principal = retrievePrincipal(request);
        if (principal != null) {
            String username = principal.getName();
            Performer performer = performerService.retrieveByUsername(username);
            GsonBuilder builder = new GsonBuilder()
                    .addSerializationExclusionStrategy(
                            ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER
                    );
            writeToResponse(response, builder, performer);
            return;
        }
    }

    @RequestMapping("my/password")
    public void changeMyPassword(@RequestParam(value = "new_password", required = false)
                                         String newPassword,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        if (!newPassword.matches(".{8,}")) {
            this.sendDefaultJson(response, false, "Weak password!");
            return;
        }
        Principal principal = retrievePrincipal(request);
        if (principal != null) {
            CustomUser user = userService.retrieveByName(principal.getName());
            newPassword = encoder.encode(newPassword);
            user.setPassword(newPassword);
            userService.update(user);
        }
        this.sendDefaultJson(response, true, "");
    }

    /**
     * Does some thing in old style.
     *
     * @deprecated use AuthenticationDetailsWrapper {@link #retrievePrincipal(HttpServletRequest)} instead.
     */
    @Deprecated
    private Principal retrievePrincipal(HttpServletRequest request) {
        Authentication auth = authenticationWrapper.getAuthentication(request);
        if (auth != null) {
            Principal principal = (Principal) auth.getPrincipal();
            return principal;
        }
        return null;
    }
}
