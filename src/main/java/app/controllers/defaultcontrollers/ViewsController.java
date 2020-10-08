package app.controllers.defaultcontrollers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ViewsController {

    @RequestMapping("/")
    public String mainPage() {
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
