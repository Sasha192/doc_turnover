package app.controllers.customtenant;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class Page404Controller {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFoundException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("status");
        modelAndView.addObject("msg", "Сторінку не знайдено");
        modelAndView.addObject("status", "404");
        return modelAndView;
    }
}
