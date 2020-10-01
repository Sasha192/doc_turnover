package app.controllers;

import app.models.basic.Performer;
import app.security.models.SimpleRole;
import app.security.wrappers.IPerformerWrapper;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewsController {

    @Autowired
    private IPerformerWrapper performerWrapper;

    @RequestMapping("/")
    public String testMapping() {
        return "test";
    }

    @RequestMapping("/archive")
    public String archiveMap(ModelMap map) {
        return "archive";
    }

    @RequestMapping("/myboard")
    public String profileMap(ModelMap map) {
        return "myboard";
    }

    @RequestMapping("/auth")
    public String authMap(ModelMap map) {
        return "auth";
    }

    @RequestMapping("/teamboard")
    public String teamboardMap(ModelMap map) {
        return "tmboard";
    }

    @RequestMapping("/chart")
    public String chartMap(ModelMap map) {
        return "charts";
    }

    @RequestMapping("/CLpanel")
    public ModelAndView clPanel(ModelAndView modelAndView,
                                HttpServletRequest request) {
        Performer performer = performerWrapper.retrievePerformer(request);
        Set<SimpleRole> roles = performer.getRoles();
        if (roles.contains(SimpleRole.ADMIN)
                || roles.contains(SimpleRole.G_MANAGER)) {
            modelAndView.setViewName("cl_panel");
            return modelAndView;
        }
        // @TODO : send redirect to another controller,
        //  that will handle all
        // such cases!
        modelAndView.setViewName("status");
        modelAndView.addObject("msg", "Доступ заблоковано");
        modelAndView.addObject("status", "403");
        return modelAndView;
    }
}
