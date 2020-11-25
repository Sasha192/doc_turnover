package app.security.controllers;

import app.controllers.customtenant.JsonSupportController;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.serialization.ExcludeStrategies;
import app.security.models.auth.UserInfo;
import app.security.wrappers.ICustomUserWrapper;
import app.security.wrappers.IPerformerWrapper;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/com/myinfo")
public class UserInfoController
        extends JsonSupportController {

    private final IPerformerWrapper performerWrapper;

    private final ICustomUserWrapper userWrapper;

    public UserInfoController(IPerformerWrapper performerWrapper,
                              ICustomUserWrapper userWrapper) {
        this.performerWrapper = performerWrapper;
        this.userWrapper = userWrapper;
    }


    @RequestMapping(method = RequestMethod.GET)
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
            jInfo.add("name", new JsonPrimitive(performer.getName()));
        } else {
            jInfo.add("name", new JsonPrimitive(info.getName()));
        }
        jInfo.add("success", new JsonPrimitive(true));
        sendDefaultJson(response, jInfo);
    }
}
