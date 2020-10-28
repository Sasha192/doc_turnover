package app.controllers.defaultcontrollers;

import app.customtenant.service.interfaces.IPerformerService;
import app.security.wrappers.ICustomUserWrapper;
import app.tenantconfiguration.interfaces.ITenantCreatorService;
import dev.morphia.Datastore;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewsController {

    @Autowired
    private ITenantCreatorService tenantService;
    @Autowired
    private ICustomUserWrapper userWrapper;
    @Autowired
    private Datastore morphia;

    @Autowired
    private IPerformerService performerService;

    @RequestMapping("/")
    public String mainPage(HttpServletRequest request)
            throws SQLException {
        return "main";
    }

    @RequestMapping("/my")
    public String myPage() {
        return "my";
    }

    @RequestMapping("/gpanel")
    public ModelAndView clPanel(ModelAndView modelAndView,
                                HttpServletRequest request) {
        modelAndView.setViewName("gpanel");
        return modelAndView;
        /*Performer performer = performerWrapper.retrievePerformer(request);
        SimpleRole roles = performer.getRoles();
        if (roles.equals(SimpleRole.ADMIN)
                || roles.equals(SimpleRole.G_MANAGER)) {

        }
        // @TODO : send redirect to another controller,
        //  that will handle all
        // such cases!
        modelAndView.setViewName("status");
        modelAndView.addObject("msg", "Доступ заблоковано");
        modelAndView.addObject("status", "403");
        return modelAndView;*/
    }
}
