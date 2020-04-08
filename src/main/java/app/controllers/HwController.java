package app.controllers;

import app.models.CoreProperty;
import app.service.CorePropertySevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HwController {

    @Autowired
    private CorePropertySevice corePropertySevice;

    @RequestMapping("/helloworld")
    public String handler(Model model) {
        CoreProperty coreProperty = new CoreProperty();
        coreProperty.setName("core");
        coreProperty.setValue("property");
        Integer id = corePropertySevice.insert(coreProperty);
        model.addAttribute("helloWorld", new String("hello"));
        return "helloworld";
    }

}
