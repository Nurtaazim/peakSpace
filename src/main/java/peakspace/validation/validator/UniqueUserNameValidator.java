package peakspace.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import peakspace.entities.User;
import peakspace.repository.UserRepository;
import peakspace.validation.UniqueUserNameValidation;
@RequiredArgsConstructor
public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserNameValidation, String> {
    private final UserRepository userRepository;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        for (User user : userRepository.findAll()) {
            if (!user.getUsername().equals(s)) return false;
        }
        return true;
    }
}
