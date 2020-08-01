package app.controllers;

import app.controllers.dto.IEntityDtoMapper;
import app.controllers.dto.PerformerDto;
import app.models.Department;
import app.models.Performer;
import app.models.serialization.ExcludeStrategies;
import app.service.IDepartmentService;
import app.service.IPerformerService;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping(value = {"/list", "/list/{depo_id}"})
    public void list(HttpServletResponse response,
                     @PathVariable(required = false) Long departmentId) throws IOException {
        List<Performer> performers = null;
        if (departmentId != null) {
            performers = performerService.findByDepartmentId(departmentId);
        } else {
            performers = performerService.findAll();
        }
        GsonBuilder builder = new GsonBuilder()
                .addSerializationExclusionStrategy(
                        ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER
                ).setPrettyPrinting();
        writeToResponse(response, builder, performers);
    }

    @PostMapping("/modify/department")
    public void changeDepartment(HttpServletResponse response,
                                 @RequestParam("performer_id") Integer performerId,
                                 @RequestParam("department_id") Integer departmentId) {
        Department newDepartment = departmentService.findOne(departmentId);
        Performer performer = performerService.findOne(performerId);
        performer.setDepartment(newDepartment);
        performerService.update(performer);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createPerformer(HttpServletResponse response,
                                @Validated @RequestBody PerformerDto dto) {
        Performer performer = (Performer) mapper.getEntity(dto);
        performerService.create(performer);
    }
}
