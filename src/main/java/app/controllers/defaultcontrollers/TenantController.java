package app.controllers.defaultcontrollers;

import app.configuration.spring.constants.Constants;
import app.controllers.customtenant.JsonSupportController;
import app.controllers.dto.TenantDto;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.extapis.GMailService;
import app.customtenant.service.interfaces.IPerformerService;
import app.security.models.SimpleRole;
import app.security.models.auth.CustomUser;
import app.security.models.auth.UserInfo;
import app.security.wrappers.ICustomUserWrapper;
import app.tenantconfiguration.TenantContext;
import app.tenantconfiguration.interfaces.ITenantCreatorService;
import app.tenantdefault.models.TenantInfoEntity;
import app.tenantdefault.service.ITenantService;
import dev.morphia.Datastore;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bson.BsonBoolean;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/main/tenants")
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

    @Autowired
    public TenantController(ITenantCreatorService tenantCreatorService,
                            Datastore morphia,
                            ICustomUserWrapper userWrapper,
                            ITenantService tenantService,
                            IPerformerService performerService,
                            GMailService mailService) {
        this.tenantCreatorService = tenantCreatorService;
        this.morphia = morphia;
        this.userWrapper = userWrapper;
        this.tenantService = tenantService;
        this.performerService = performerService;
    }

    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(HttpServletRequest request,
                       HttpServletResponse response,
                       @Validated @RequestBody TenantDto dto) {
        TenantInfoEntity info = new TenantInfoEntity(dto);
        CustomUser user = userWrapper.retrieveUser(request);
        Set<String> tenants = user.getTenants();
        if ((tenants != null && !tenants.isEmpty())
                || Constants.TENANTS_NUMBER > 1) {
            sendDefaultJson(response,
                    false,
                    "Операцію заблоковано : сервіс у тестовому режимі"
            );
            return;
        }
        try {
            String tenantId = tenantCreatorService.create();
            info.setTenantId(tenantId);
            info.setOwnerId(user.getId());
            morphia.save(info);
            if (tenants == null) {
                tenants = new HashSet<>();
            }
            tenants = new HashSet<>(tenants);
            tenants.add(tenantId);
            user.setTenantsId(tenants);
            userWrapper.update(user);
            UserInfo uInfo = user.getUserInfo();
            uInfo.setActiveTenant(tenantId);
            userWrapper.update(uInfo);
            TenantContext.setTenant(uInfo.getActiveTenant());
            Performer performer = new Performer(user);
            performer.setRoles(SimpleRole.ADMIN);
            performerService.create(performer);
            request.getSession(true).setAttribute(Constants.PERFORMER_SESSION_KEY, performer);
            sendDefaultJson(response, true, tenantId);
        } catch (SQLException ex) {
            sendDefaultJson(response, false, ERROR_MSG);
        }
    }

    @RequestMapping(value = "/remove",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void remove(HttpServletResponse response,
                       HttpServletRequest request,
                       @RequestParam("tenantId") String tenantId) {
        try {
            TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
            CustomUser user = userWrapper.retrieveUser(request);
            TenantInfoEntity entity = tenantService.findById(tenantId);
            if (entity.getOwnerId().equals(user.getId())) {
                tenantService.remove(tenantId);
                tenantCreatorService.remove(tenantId);
            }
            user.getTenants().remove(tenantId);
            userWrapper.update(user);
            UserInfo info = user.getUserInfo();
            info.setActiveTenant(TenantContext.PHANTOM_TENANT_IDENTIFIER);
            userWrapper.update(info);
            sendDefaultJson(response, true, "");
        } catch (SQLException e) {
            sendDefaultJson(response, false, "Помилка на сервері. Спробуйте пізніше");
        }
    }

    @GetMapping(value = "/connect")
    public void connect(@RequestParam("tenantId") String tenantId,
                        HttpServletRequest request,
                        HttpServletResponse response)
            throws IOException {
        CustomUser user = userWrapper.retrieveUser(request);
        Set<String> tenants = user.getTenants();
        HttpSession session = request.getSession();
        if (tenants.contains(tenantId) && session != null) {
            UserInfo info = user.getUserInfo();
            info.setActiveTenant(tenantId);
            userWrapper.update(info);
            session.setAttribute(Constants.TENANT_SESSION_ID, tenantId);
            TenantContext.setTenant(tenantId);
            Performer performer = performerService.retrieveByUserId(user.getId());
            if (performer == null) {
                performer = new Performer(user);
                performerService.create(performer);
                TenantInfoEntity tenant = tenantService.findById(tenantId);
                tenant.setNumber(tenant.getNumber() + 1);
                tenantService.update(tenant);
            }
            session.setAttribute(Constants.PERFORMER_SESSION_KEY, performer);
            TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
            sendDefaultJson(response, true, "");
        } else {
            sendDefaultJson(response, false, "");
        }
    }

    @GetMapping(value = "/invite/connect")
    public void connectInvite(@RequestParam("tenantId") String tenantId,
                              HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {
        CustomUser user = userWrapper.retrieveUser(request);
        if (user == null) {
            sendDefaultJson(response, false, "Будь ласка, увійдіть до системи і спробуйте знову!");
            return;
        }
        Set<String> userTenants = user.getTenants();
        if (userTenants == null) {
            userTenants = new HashSet<>();
        }
        if (!userTenants.contains(tenantId)) {
            TenantInfoEntity tenant = tenantService.findById(tenantId);
            if (tenant == null) {
                sendDefaultJson(response, false, "Шутки шуткуєш?");
                return;
            }
            userTenants.add(tenantId);
            user.setTenantsId(userTenants);
            userWrapper.update(user);
        }
        try {
            request.getRequestDispatcher("/main/tenants/connect")
                    .forward(request, response);
        } catch (ServletException e) {
            sendDefaultJson(response, false, "Помилка на сервері. Будь ласка, спробуйте пізніше");
        }
    }

    @GetMapping(value = "/invite/connect/view")
    public String connectInviteView(@RequestParam("tenantId") String tenantId,
                              HttpServletRequest request)
            throws IOException {
        CustomUser user = userWrapper.retrieveUser(request);
        if (user == null) {
            return "main";
        }
        Set<String> userTenants = user.getTenants();
        if (userTenants == null) {
            userTenants = new HashSet<>();
        }
        if (!userTenants.contains(tenantId)) {
            TenantInfoEntity tenant = tenantService.findById(tenantId);
            if (tenant == null) {
                return "index";
            }
            userTenants.add(tenantId);
            user.setTenantsId(userTenants);
            userWrapper.update(user);
        }
        connectView(tenantId, user, request.getSession());
        return "index";
    }

    private void connectView(String tenantId,
                        CustomUser user, HttpSession session)
            throws IOException {
        Set<String> tenants = user.getTenants();
        if (tenants.contains(tenantId) && session != null) {
            UserInfo info = user.getUserInfo();
            info.setActiveTenant(tenantId);
            userWrapper.update(info);
            session.setAttribute(Constants.TENANT_SESSION_ID, tenantId);
            TenantContext.setTenant(tenantId);
            Performer performer = performerService.retrieveByUserId(user.getId());
            if (performer == null) {
                performer = new Performer(user);
                performerService.create(performer);
                session.setAttribute(Constants.PERFORMER_SESSION_KEY, performer);
                TenantInfoEntity tenant = tenantService.findById(tenantId);
                tenant.setNumber(tenant.getNumber() + 1);
                tenantService.update(tenant);
            }
            TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
        }
    }

    @RequestMapping(value = "/com/my/list")
    public void myTenantsList(HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {
        CustomUser u = userWrapper.retrieveUser(request);
        Set<String> tenants = u.getTenants();
        Collection<Document> entities = new LinkedList<>();
        if (tenants != null) {
            entities = tenantService.findMyTenants(tenants);
        }
        String active = u.getUserInfo().getActiveTenant();
        Optional<Document> entity = entities
                .stream()
                .filter(t -> t.get("_id")
                        .equals(active))
                .findAny();
        entity.ifPresent(bsonDocument -> bsonDocument
                .put("active", new BsonBoolean(Boolean.TRUE))
        );
        if (!entities.isEmpty()) {
            sendDefaultJson(response, entities);
        } else {
            sendDefaultJson(response, new LinkedList<>());
        }
    }

}
