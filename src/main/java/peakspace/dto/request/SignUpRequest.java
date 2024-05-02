package peakspace.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import peakspace.validation.EmailValidation;
import peakspace.validation.PasswordValidation;
import peakspace.validation.UniqueEmailValidation;
import peakspace.validation.UniqueUserNameValidation;

@Builder
public record SignUpRequest(
        @NotNull
        String lastName,
        @NotNull
        String firstName,
        @UniqueUserNameValidation
        String userName,
        @EmailValidation
        @UniqueEmailValidation
        String email,
        @PasswordValidation
        String password
) {
}
