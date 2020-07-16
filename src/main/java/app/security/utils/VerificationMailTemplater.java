package app.security.utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class VerificationMailTemplater {

    public class VerificationCode {

        private String verificationCode;

        public VerificationCode(final String verificationCode) {
            this.verificationCode = verificationCode;
        }

        public String getVerificationCode() {
            return this.verificationCode;
        }

        public void setVerificationCode(final String verificationCode) {
            this.verificationCode = verificationCode;
        }
    }

    @Autowired
    @Qualifier("for.email.template")
    private Handlebars handlebars;

    public String render(String verificationCode) throws IOException {
        Template template = handlebars.compile("verification_email_template");
        return template.apply(new VerificationCode(verificationCode));
    }
}
