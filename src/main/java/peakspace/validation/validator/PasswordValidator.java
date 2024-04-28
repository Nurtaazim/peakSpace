package peakspace.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import peakspace.validation.PasswordValidation;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s.length() < 8 || s.length() > 20) {
            return false;
        }

        // Проверка наличия цифр
        if (!containsDigit(s)) {
            return false;
        }

        // Проверка наличия букв в верхнем регистре
        if (!containsUpperCaseLetter(s)) {
            return false;
        }

        // Проверка наличия букв в нижнем регистре
        if (!containsLowerCaseLetter(s)) {
            return false;
        }

        return true;

    }
    // Дополнительные проверки, если нужно
    private static boolean containsDigit(String s) {
        return s.matches(".*\\d.*");
    }

    private static boolean containsUpperCaseLetter(String s) {
        return s.matches(".*[A-Z].*");
    }

    private static boolean containsLowerCaseLetter(String s) {
        return s.matches(".*[a-z].*");
    }
}
