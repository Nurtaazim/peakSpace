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
    public boolean isValid(String value, ConstraintValidatorContext context) {
        for (User user : userRepository.findAll()) {
            if (user.getThisUserName().equalsIgnoreCase(value)) return false;
        }
        return true;
    }

}
