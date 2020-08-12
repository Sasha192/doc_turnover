package app.controllers;

import app.controllers.dto.PerformerDto;
import app.controllers.dto.mappers.IEntityDtoMapper;
import app.models.basic.Department;
import app.models.basic.Performer;
import app.models.mysqlviews.BriefPerformer;
import app.models.serialization.ExcludeStrategies;
import app.service.interfaces.IBriefPerformerService;
import app.service.interfaces.IDepartmentService;
import app.service.interfaces.IPerformerService;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/performers")
public class PerformersNavigationController extends JsonSupportController {

    @Autowired
    @Qualifier("performer_mapper")
    private IEntityDtoMapper mapper;

    @Autowired
    private IPerformerService performerService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IBriefPerformerService briefPerformerService;

    @GetMapping(value = {"/list", "/list/{depo_id}"})
    public void list(HttpServletResponse response,
                     @PathVariable(required = false) Long departmentId) throws IOException {
        List<BriefPerformer> performers = null;
        if (departmentId != null) {
            performers = briefPerformerService
                    .findByDepartmentId(departmentId);
        } else {
            performers = briefPerformerService
                    .findAll();
        }
        GsonBuilder builder = new GsonBuilder()
                .addSerializationExclusionStrategy(
                        ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER
                ).setPrettyPrinting();
        writeToResponse(response, builder, performers);
    }

    @RequestMapping(value = "/modify/department", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changeDepartment(HttpServletResponse response,
                                 @RequestParam("performer_id") Integer performerId,
                                 @RequestParam("department_id") Integer departmentId) {
        Department newDepartment = departmentService.findOne(departmentId);
        Performer performer = performerService.findOne(performerId);
        performer.setDepartmentId(newDepartment.getId());
        performerService.update(performer);
    }

    @RequestMapping(value = "/modify/role", method = RequestMethod.POST)
    public void changeDepartment(HttpServletResponse response,
                                 @RequestParam("performer_id") Integer performerId,
                                 @RequestParam("role") String role) {
        return;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createPerformer(HttpServletResponse response,
                                @Validated @RequestBody PerformerDto dto) {
        Performer performer = (Performer) mapper.getEntity(dto);
        performerService.create(performer);
    }
}
