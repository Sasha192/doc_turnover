package app.controllers.defaultcontrollers;

import app.controllers.JsonSupportController;
import app.controllers.dto.TenantDto;
import app.security.controllers.filters.RestrictRedirectUtil;
import app.security.models.auth.CustomUser;
import app.security.wrappers.ICustomUserWrapper;
import app.tenantconfiguration.TenantContext;
import app.tenantconfiguration.interfaces.ITenantCreatorService;
import app.tenantdefault.models.TenantInfoEntity;
import dev.morphia.Datastore;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tenants")
public class TenantController extends JsonSupportController {

    private static final String ERROR_MSG =
            "Could not create archive."
                    + "It is very low probability not to create archive."
                    + "Unfortunately something go wrong."
                    + "Please try later or contact with us.";

    private ITenantCreatorService tenantService;

    private Datastore morphia;

    private ICustomUserWrapper userWrapper;

    @Autowired
    public TenantController(ITenantCreatorService tenantService,
                            Datastore morphia,
                            ICustomUserWrapper userWrapper) {
        this.tenantService = tenantService;
        this.morphia = morphia;
        this.userWrapper = userWrapper;
    }

    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(HttpServletRequest request,
                       HttpServletResponse response,
                       @Validated @RequestBody TenantDto dto) {
        TenantInfoEntity info = new TenantInfoEntity(dto);
        try {
            String tenantId = tenantService.create();
            info.setTenantId(tenantId);
            CustomUser user = userWrapper.retrieveUser(request);
            morphia.save(info);
            user.addTenant(tenantId);
            userWrapper.update(user);
            sendDefaultJson(response, true, tenantId);
        } catch (SQLException ex) {
            sendDefaultJson(response, false, ERROR_MSG);
        }
    }

    @GetMapping(value = "/connect")
    public void connect(@RequestParam("tenant_id") String tenantId,
                        HttpServletRequest request,
                        HttpServletResponse response)
            throws IOException {
        CustomUser user = userWrapper.retrieveUser(request);
        Set<String> tenants = user.getTenants();
        if (tenants.contains(tenantId)) {
            TenantContext.setTenant(tenantId);
        } else {
            RestrictRedirectUtil.restrict(response);
        }
    }

}
