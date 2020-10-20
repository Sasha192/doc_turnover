package app.controllers.defaultcontrollers;

import app.configuration.spring.constants.Constants;
import app.controllers.customtenant.JsonSupportController;
import app.controllers.dto.TenantDto;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.interfaces.IPerformerService;
import app.security.models.auth.CustomUser;
import app.security.wrappers.ICustomUserWrapper;
import app.security.wrappers.IPerformerWrapper;
import app.tenantconfiguration.TenantContext;
import app.tenantconfiguration.interfaces.ITenantCreatorService;
import app.tenantdefault.models.TenantInfoEntity;
import app.tenantdefault.service.ITenantService;
import dev.morphia.Datastore;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tenants")
public class TenantController extends JsonSupportController {

    private static final String ERROR_MSG =
            "Could not create archive."
                    + "Unfortunately something go wrong."
                    + "Please try later or contact with us.";

    private ITenantCreatorService tenantCreatorService;

    private Datastore morphia;

    private ICustomUserWrapper userWrapper;

    private final ITenantService tenantService;

    private final IPerformerService performerService;

    private final IPerformerWrapper performerWrapper;

    @Autowired
    public TenantController(ITenantCreatorService tenantCreatorService,
                            Datastore morphia,
                            ICustomUserWrapper userWrapper,
                            ITenantService tenantService,
                            IPerformerService performerService,
                            IPerformerWrapper performerWrapper) {
        this.tenantCreatorService = tenantCreatorService;
        this.morphia = morphia;
        this.userWrapper = userWrapper;
        this.tenantService = tenantService;
        this.performerService = performerService;
        this.performerWrapper = performerWrapper;
    }

    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(HttpServletRequest request,
                       HttpServletResponse response,
                       @Validated @RequestBody TenantDto dto) {
        TenantInfoEntity info = new TenantInfoEntity(dto);
        CustomUser user = userWrapper.retrieveUser(request);
        try {
            String tenantId = tenantCreatorService.create();
            info.setTenantId(tenantId);
            info.setOwnerId(user.getId());
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
        HttpSession session = request.getSession();
        if (tenants.contains(tenantId) && session != null) {
            TenantContext.setTenant(tenantId);
            session.setAttribute(Constants.TENANT_SESSION_ID, tenantId);
            sendDefaultJson(response, true, "");
            return;
        } else {
            sendDefaultJson(response, false, "");
        }
    }

    @GetMapping(value = "/connect/invite")
    public void connectInvite(@RequestParam("tenant_id") String tenantId,
                              HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {
        CustomUser user = userWrapper.retrieveUser(request);
        TenantInfoEntity tenant = tenantService.findById(tenantId);
        HttpSession session = request.getSession();
        if (tenant != null && session != null) {
            user.addTenant(tenant.getTenantId());
            TenantContext.setTenant(tenantId);
            session.setAttribute(Constants.TENANT_SESSION_ID, tenantId);
            Performer performer = new Performer(user);
            performerService.create(performer);
            performerWrapper.setPerformer(performer, session);
            sendDefaultJson(response, true, "");
            return;
        } else {
            sendDefaultJson(response, false, "");
        }
    }

    @RequestMapping(value = "/com/list/{page_id}")
    public void tenantsList(HttpServletResponse response,
                            @PathVariable("page_id") Integer page)
            throws IOException {
        sendDefaultJson(response, tenantService.findPageableOpen(page.intValue()));
    }

    @RequestMapping(value = "/com/my/list")
    public void myTenantsList(HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {
        CustomUser u = userWrapper.retrieveUser(request);
        sendDefaultJson(response, tenantService.findMyTenants(u.getTenants()));
    }

    @RequestMapping(value = "/com/list/{page_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void tenantsListFilter(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @RequestBody BsonDocument filter,
                                  @PathVariable("page_id") Integer page)
            throws IOException {
        sendDefaultJson(
                response, tenantService.findPageableFilter(
                        page.intValue(), filter)
        );
    }

}
