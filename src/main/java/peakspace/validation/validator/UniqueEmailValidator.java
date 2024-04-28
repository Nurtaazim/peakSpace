package peakspace.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import peakspace.entities.User;
import peakspace.repository.UserRepository;
import peakspace.validation.UniqueEmailValidation;
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmailValidation, String> {
    private final UserRepository userRepository;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        for (User user : userRepository.findAll()) {
            if (user.getUsername().equalsIgnoreCase(s)) return false;
        }
        return true;
    }
}
