package peakspace.dto.request;

import jakarta.validation.constraints.NotBlank;
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

        @NotBlank(message = "Пароль обязателен.")
        @Size(min = 6, message = "Пароль должен быть длиной не менее 6 символов.")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
                message = "Пароль должен содержать хотя бы одну цифру, одну строчную букву и одну заглавную букву.")
        String password
) {
}
