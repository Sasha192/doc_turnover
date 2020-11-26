package app.controllers.customtenant;

import app.configuration.spring.constants.Constants;
import app.customtenant.models.basic.Department;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.serialization.ExcludeStrategies;
import app.customtenant.service.interfaces.IDepartmentService;
import app.security.models.SimpleRole;
import app.security.wrappers.PerformerWrapper;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/com/departments")
public class DepartmentsNavigationController
        extends JsonSupportController {

    private static final GsonBuilder BUILDER =
            new GsonBuilder()
                    .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
                    .setDateFormat(Constants.DATE_FORMAT.toPattern())
                    .setPrettyPrinting();

    @Autowired
    private PerformerWrapper wrapper;

    @Autowired
    private IDepartmentService departmentService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public void create(HttpServletResponse res, HttpServletRequest request,
                       @RequestParam("name") String depName,
                       @RequestParam(value = "parent", required = false) Long parent) {
        Performer performer = wrapper.retrievePerformer(request);
        if (allowCreationOp(performer.getRoles())) {
            Department newDepa = new Department();
            newDepa.setName(depName);
            if (parent != null) {
                newDepa.setParentDepartmentId(parent);
            }
            departmentService.create(newDepa);
            sendDefaultJson(res, true, "");
        } else {
            sendDefaultJson(res, false, "Access Denied");
        }
    }

    private boolean allowCreationOp(SimpleRole roles) {
        return roles.equals(SimpleRole.G_MANAGER)
                || roles.equals(SimpleRole.ADMIN);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(HttpServletResponse response,
                     @RequestParam(value = "parent", required = false) Long parent)
            throws IOException {
        List<Department> depos = null;
        if (parent == null) {
            depos = departmentService.findAll();
        } else {
            depos = departmentService.retrieveByParent(parent);
        }
        writeToResponse(response,BUILDER, depos);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public void modify(HttpServletResponse response, HttpServletRequest request,
                       @RequestParam(value = "newName") String name,
                       @RequestParam(value = "depId") Long depoId) {
        Performer performer = wrapper.retrievePerformer(request);
        if (allowCreationOp(performer.getRoles())) {
            departmentService.changeDeparmentName(name, depoId);
        } else {
            sendDefaultJson(response, false, "Access Denied");
        }
    }

    @GetMapping(value = "/remove")
    public void remove(HttpServletResponse response, HttpServletRequest request,
                       @RequestParam(value = "departmentId") Long depoId) {
        Performer performer = wrapper.retrievePerformer(request);
        if (allowCreationOp(performer.getRoles())) {
            departmentService.deleteById(depoId);
        } else {
            sendDefaultJson(response, false, "Access Denied");
        }
    }

}
