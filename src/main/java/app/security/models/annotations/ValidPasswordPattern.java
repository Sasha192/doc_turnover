package app.security.models.annotations;

import app.security.models.validator.PasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPasswordPattern {

    String message() default "Incorrect password format";

    String pattern() default ".{8,}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
