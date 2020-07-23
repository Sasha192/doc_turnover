package app.security.models.validator;

import app.security.models.annotations.ValidPasswordPattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator
        implements ConstraintValidator<ValidPasswordPattern, Object> {

    private String pattern;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o instanceof String) {
            String patternMatching = (String) o;
            return patternMatching.matches(pattern);
        }
        return false;
    }

    @Override
    public void initialize(ValidPasswordPattern constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }
}
