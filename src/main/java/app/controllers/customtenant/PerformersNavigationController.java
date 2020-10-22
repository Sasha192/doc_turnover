package app.controllers.customtenant;

import app.controllers.dto.mappers.IEntityDtoMapper;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.serialization.ExcludeStrategies;
import app.customtenant.service.interfaces.IBriefPerformerService;
import app.customtenant.service.interfaces.IDepartmentService;
import app.customtenant.service.interfaces.IPerformerService;
import app.security.models.SimpleRole;
import app.security.service.IUserService;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/performers")
public class PerformersNavigationController extends JsonSupportController {

    private final IEntityDtoMapper mapper;

    private final IPerformerService performerService;

    private final IDepartmentService departmentService;

    private final IBriefPerformerService briefPerformerService;

    private final IUserService userService;

    @Autowired
    public PerformersNavigationController(@Qualifier("performer_mapper") IEntityDtoMapper mapper,
                                          IPerformerService performerService,
                                          IDepartmentService departmentService,
                                          IBriefPerformerService briefPerformerService,
                                          IUserService userService) {
        this.mapper = mapper;
        this.performerService = performerService;
        this.departmentService = departmentService;
        this.briefPerformerService = briefPerformerService;
        this.userService = userService;
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
        GsonBuilder builder = new GsonBuilder()
                .addSerializationExclusionStrategy(
                        ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
                .setPrettyPrinting();
        writeToResponse(response, builder, performers);
    }

    @RequestMapping(value = "/modify/department", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changeDepartment(HttpServletResponse response,
                                 @RequestParam("performer_id") Long performerId,
                                 @RequestParam("department_id") Long departmentId) {
        performerService.updatePerformerDepartment(performerId, departmentId);
        sendDefaultJson(response, true, "");
    }

    @RequestMapping(value = "/modify/role", method = RequestMethod.POST)
    public void changeRole(HttpServletResponse response,
                                 @RequestParam("performer_id") Long performerId,
                                 @RequestParam("role") String role) {
        SimpleRole newRole = SimpleRole.valueOf(role);
        performerService.updatePerformerRole(performerId, newRole);
        sendDefaultJson(response, true, "");
    }

    @PostMapping(value = "/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void removePerformer(HttpServletResponse response,
                                @RequestParam("performer_id") Long performerId) {
        performerService.deleteById(performerId);
        sendDefaultJson(response, true, "");
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
