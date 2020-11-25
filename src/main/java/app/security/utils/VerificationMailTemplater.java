package app.security.utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

@Component
public class VerificationMailTemplater {

    @Autowired
    @Qualifier("for.email.template")
    private Handlebars handlebars;

    public String render(String verificationCode) throws IOException {
        Template template = handlebars.compile("verificatonMail");
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("verificationCode", verificationCode);
        return template.apply(modelMap);
    }

    public String render(ModelMap modelMap) throws IOException {
        Template template = handlebars.compile("bad_request");
        return template.apply(modelMap);
    }
}
