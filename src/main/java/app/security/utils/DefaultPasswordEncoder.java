package app.security.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultPasswordEncoder implements PasswordEncoder {

    //@TODO CHANGE PASSWORD ENCODER WITH SALT!!!
    // @TODO CHANGE SHA -> Something faster

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence charSequence) {
        return encoder.encode(charSequence);
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encoder.matches(charSequence, s);
    }
}
