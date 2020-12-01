package app.controllers.defaultcontrollers;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.io.IOException;

@Component
public class InvitationMailTemplater {

    @Autowired
    @Qualifier("for.email.template")
    private Handlebars handlebars;

    public String render(String inviteOwner, String tenantName, String linkRef) throws IOException {
        Template template = handlebars.compile("inviteMail");
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("invite_owner", inviteOwner);
        modelMap.addAttribute("tenant_name", tenantName);
        modelMap.addAttribute("link_ref", linkRef);
        return template.apply(modelMap);
    }

    public String render(ModelMap modelMap) throws IOException {
        Template template = handlebars.compile("bad_request");
        return template.apply(modelMap);
    }

}
