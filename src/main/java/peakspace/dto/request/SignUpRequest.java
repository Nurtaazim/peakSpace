package peakspace.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import peakspace.validation.EmailValidation;
import peakspace.validation.PasswordValidation;
import peakspace.validation.UserNameValidation;

@Builder
public record SignUpRequest(
        @NotNull
        String lastName,
        @NotNull
        String firstName,
        @UserNameValidation
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Имя пользователя должно содержать только латинские буквы")
        @Size(min = 3, max = 20, message = "Имя пользователя должно содержать от 3 до 20 символов")
        String userName,
        @EmailValidation
        String email,
        @PasswordValidation
        String password
) {
}
