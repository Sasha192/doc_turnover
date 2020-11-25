package app.controllers.defaultcontrollers;

import app.controllers.customtenant.JsonSupportController;
import app.customtenant.service.extapis.GMailService;
import app.security.models.auth.CustomUser;
import app.security.wrappers.ICustomUserWrapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;

@Controller
@RequestMapping("/com/tenants")
public class TenantInviteController
        extends JsonSupportController {

    private final ICustomUserWrapper userWrapper;

    private final GMailService mailService;

    public TenantInviteController(ICustomUserWrapper userWrapper,
                                  GMailService mailService) {
        this.userWrapper = userWrapper;
        this.mailService = mailService;
    }

    @PostMapping(value = "/invite",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void invite(@RequestBody String body,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        JsonElement element = JsonParser.parseString(body);
        if (element.isJsonObject()) {
            JsonObject o = element.getAsJsonObject();
            String tenantId = o.get("tenantId").getAsString();
            JsonArray emails = o.get("emails").getAsJsonArray();
            CustomUser user = userWrapper.retrieveUser(request);
            if (user.getTenants().contains(tenantId)) {
                Iterator<JsonElement> iterator = emails.iterator();
                while (iterator.hasNext()) {
                    String email = iterator.next().getAsString();
                    String baseUrl = String.format("%s://%s:%d/tenants/invite/connect?tenantId=%s",
                            request.getScheme(),
                            request.getServerName(),
                            request.getServerPort(),
                            tenantId);
                    mailService.send(email, "INVITE LINK", baseUrl);
                }
            }
        }
        sendDefaultJson(response, false, "");
    }
}
