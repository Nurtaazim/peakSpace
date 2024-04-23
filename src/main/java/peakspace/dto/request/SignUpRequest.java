package peakspace.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import peakspace.validation.EmailValidation;
import peakspace.validation.PasswordValidation;
import peakspace.validation.UniqueUserNameValidation;

@Builder
public record SignUpRequest(
        @NotNull
        String surName,
        @NotNull
        String name,
        @UniqueUserNameValidation
        String userName,
        @EmailValidation
        String email,
        @PasswordValidation
        String password
) {
}
