package app.security.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthenticationExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(final HttpServletRequest req, final Exception ex) {
        final ModelAndView mav = new ModelAndView();
        mav.addObject("msg", ex.getMessage());
        mav.addObject("exception_code", 500);
        mav.setViewName("exception");
        return mav;
    }
}
