package app.controllers;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.io.Writer;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class LayoutAdvice {

    @ModelAttribute("layout")
    public Mustache.Lambda layout() {
        return new Layout();
    }

    public class Layout implements Mustache.Lambda {

        String body;

        @Override
        public void execute(Template.Fragment fragment, Writer writer) throws IOException {
            body = fragment.execute();
        }
    }
}

