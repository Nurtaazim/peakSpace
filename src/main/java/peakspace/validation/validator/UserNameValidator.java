package peakspace.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import peakspace.entities.User;
import peakspace.repository.UserRepository;
import peakspace.validation.UserNameValidation;

@RequiredArgsConstructor
public class UserNameValidator implements ConstraintValidator<UserNameValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.length() < 1 || value.length() > 15) {
            return false;
        }

        // Проверка наличия букв в верхнем регистре
        if (!containsUpperCaseLetter(value)) {
            return false;
        }

        // Проверка наличия букв в нижнем регистре
        if (!containsLowerCaseLetter(value)) {
            return false;
        }
        return true;
    }

    private static boolean containsUpperCaseLetter(String s) {
        return s.matches(".*[A-Z].*");
    }

    private static boolean containsLowerCaseLetter(String s) {
        return s.matches(".*[a-z].*");
    }

}
