package app.controllers.defaultcontrollers;

import app.controllers.customtenant.JsonSupportController;
import app.customtenant.service.extapis.GMailService;
import app.customtenant.service.impl.ExecutionService;
import app.security.models.auth.CustomUser;
import app.security.wrappers.ICustomUserWrapper;
import app.tenantconfiguration.TenantContext;
import app.tenantdefault.models.TenantInfoEntity;
import app.tenantdefault.service.ITenantService;
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
import java.util.Iterator;

@Controller
@RequestMapping("/com/tenants")
public class TenantInviteController
        extends JsonSupportController {

    private final ICustomUserWrapper userWrapper;

    private final GMailService mailService;

    private final InvitationMailTemplater invitationMailTemplater;

    private final ITenantService tenantService;

    public TenantInviteController(ICustomUserWrapper userWrapper,
                                  GMailService mailService,
                                  InvitationMailTemplater invitationMailTemplater,
                                  ITenantService tenantService) {
        this.userWrapper = userWrapper;
        this.mailService = mailService;
        this.invitationMailTemplater = invitationMailTemplater;
        this.tenantService = tenantService;
    }

    @PostMapping(value = "/invite",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void invite(@RequestBody String body,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        try {
            JsonElement element = JsonParser.parseString(body);
            if (element.isJsonObject()) {
                String prev = TenantContext.getTenant();
                TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
                JsonObject o = element.getAsJsonObject();
                String tenantId = o.get("tenantId").getAsString();
                JsonArray emails = o.get("emails").getAsJsonArray();
                CustomUser user = userWrapper.retrieveUser(request);
                if (user.getTenants().contains(tenantId)) {
                    for (JsonElement jsonElement : emails) {
                        String email = jsonElement.getAsString();
                        String baseUrl = String.format("%s://%s:%d/main/tenants/invite/connect/view?tenantId=%s",
                                request.getScheme(),
                                request.getServerName(),
                                request.getServerPort(),
                                tenantId);
                        TenantInfoEntity tenantInfo = tenantService.findById(tenantId);
                        String inviteOwner = user.getUserInfo().getName();
                        String html = invitationMailTemplater.render(
                                inviteOwner,
                                tenantInfo.getCompanyName(),
                                baseUrl
                        );
                        mailService.sendMimeMessage(email, "Запрошення", html);
                    }
                }
                TenantContext.setTenant(prev);
            }
            sendDefaultJson(response, true, "");
        } catch (Exception ex) {
            sendDefaultJson(response, false, "");
        }
    }
}
