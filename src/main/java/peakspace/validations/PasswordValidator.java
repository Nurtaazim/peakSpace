package peakspace.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValidation,String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password.length() >= 4 ;
    }
}
