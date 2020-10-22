package app.controllers.customtenant;

import app.customtenant.models.basic.Performer;
import app.customtenant.models.serialization.ExcludeStrategies;
import app.security.models.auth.CustomUser;
import app.security.wrappers.ICustomUserWrapper;
import app.security.wrappers.IPerformerWrapper;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/performer")
public class PerformerController extends JsonSupportController {

    // @TODO : What should we do if connection is not via http ??? Could it be ???
    // @TODO : any operations to DB perform in queue???

    private static final Logger INTLOGGER = Logger.getLogger("intExceptionLogger");

    private final IPerformerWrapper performerWrapper;
    private final ICustomUserWrapper userWrapper;

    public PerformerController(IPerformerWrapper performerWrapper,
                               ICustomUserWrapper userWrapper) {
        this.performerWrapper = performerWrapper;
        this.userWrapper = userWrapper;
    }

    @RequestMapping("/my/info")
    public void myInfo(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        CustomUser user = userWrapper.retrieveUser(request);
        GsonBuilder builder = new GsonBuilder()
                .addSerializationExclusionStrategy(
                        ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER
                )
                .setPrettyPrinting();
        JsonObject json = builder.create()
                .toJsonTree(performer)
                .getAsJsonObject();
        JsonElement imgPath = new JsonPrimitive(user.getUserInfo().getImgPath());
        json.add("imgPath", imgPath);
        writeToResponse(response, builder, performer);
    }
}
