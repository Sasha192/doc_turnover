package app.controllers.customtenant.toChange;

import app.configuration.spring.constants.Constants;
import app.controllers.customtenant.JsonSupportController;
import app.customtenant.models.basic.Department;
import app.customtenant.models.serialization.ExcludeStrategies;
import app.customtenant.service.interfaces.IDepartmentService;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/departments")
public class DepartmentsNavigationController extends JsonSupportController {

    private static final GsonBuilder BUILDER =
            new GsonBuilder()
                    .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
            .setDateFormat(Constants.DATE_FORMAT.toPattern())
            .setPrettyPrinting();

    @Autowired
    private IDepartmentService departmentService;

    private Long rootDepartment;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public void create(HttpServletResponse res,
                       @RequestParam("name") String depName,
                       @RequestParam(value = "parent", required = false) Long parent) {
        Department newDepa = new Department();
        newDepa.setName(depName);
        if (parent == null) {
            newDepa.setParentDepartmentId(rootDepartment);
        } else {
            newDepa.setParentDepartmentId(parent);
        }
        departmentService.create(newDepa);
        sendDefaultJson(res, true, "");
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(HttpServletResponse response,
                     @RequestParam(value = "parent", required = false) Long parent)
            throws IOException {
        List<Department> depos;
        if (parent == null) {
            depos = departmentService.findAll();
        } else {
            depos = departmentService.retrieveByParent(parent);
        }
        writeToResponse(response,BUILDER, depos);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public void modify(HttpServletResponse response,
                       @RequestParam(value = "newName") String name,
                       @RequestParam(value = "depId") Long depoId) {
        departmentService.changeDeparmentName(name, depoId);
    }

}
