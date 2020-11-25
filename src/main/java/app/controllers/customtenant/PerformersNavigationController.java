package app.controllers.customtenant;

import app.customtenant.models.basic.Performer;
import app.customtenant.models.serialization.ExcludeStrategies;
import app.customtenant.service.interfaces.IPerformerService;
import app.security.models.SimpleRole;
import app.tenantconfiguration.TenantContext;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/com/performers")
public class PerformersNavigationController extends JsonSupportController {

    private static GsonBuilder builder = new GsonBuilder()
            .addSerializationExclusionStrategy(
                    ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
            .setPrettyPrinting();

    private final IPerformerService performerService;

    @Autowired
    public PerformersNavigationController(IPerformerService performerService) {
        this.performerService = performerService;
    }

    @GetMapping(value = {"/list", "/list/{depo_id}"})
    public void list(HttpServletResponse response,
                     @PathVariable(required = false) Long departmentId)
            throws IOException {
        List<Performer> performers = null;
        if (departmentId != null) {
            performers = performerService
                    .findByDepartmentId(departmentId);
        } else {
            performers = performerService
                    .findAll();
        }
        writeToResponse(response, builder, performers);
    }

    @RequestMapping(value = "/modify/department", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changeDepartment(HttpServletResponse response,
                                 @RequestBody String body) {
        JsonElement element = JsonParser.parseString(body);
        if (element.isJsonObject()) {
            JsonObject o = element.getAsJsonObject();
            long performerId = o.get("performerId").getAsLong();
            long departmentId = o.get("departmentId").getAsLong();
            performerService.updatePerformerDepartment(performerId, departmentId);
            sendDefaultJson(response, true, "");
            return;
        }
        sendDefaultJson(response, false, "");
    }

    @RequestMapping(value = "/modify/role", method = RequestMethod.POST)
    public void changeRole(HttpServletResponse response,
                                 @RequestBody String body) {
        JsonElement element = JsonParser.parseString(body);
        if (element.isJsonObject()) {
            JsonObject o = element.getAsJsonObject();
            Long performerId = o.get("performerId").getAsLong();
            String role = o.get("role").getAsString();
            SimpleRole newRole = SimpleRole.valueOf(role);
            performerService.updatePerformerRole(performerId, newRole);
            sendDefaultJson(response, true, "");
            return;
        }
        sendDefaultJson(response, false, "");
    }

    @PostMapping(value = "/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void removePerformer(HttpServletResponse response,
                                @RequestBody String body) {
        JsonElement element = JsonParser.parseString(body);
        if (element.isJsonObject()) {
            JsonObject o = element.getAsJsonObject();
            long performerId = o.get("performerId").getAsLong();
            performerService.deleteById(performerId);
            sendDefaultJson(response, true, "");
            return;
        }
        sendDefaultJson(response, false, "");
    }

    @PostMapping(value = "/undistrib", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void undistributed(HttpServletResponse response,
                                @RequestParam("performer_id") Long performerId) {
        sendDefaultJson(response, true, "");
        Performer performer = performerService.findOne(performerId);
        performer.setRoles(SimpleRole.GUEST);
        performer.setDepartmentId(null);
        performerService.update(performer);
    }
}
