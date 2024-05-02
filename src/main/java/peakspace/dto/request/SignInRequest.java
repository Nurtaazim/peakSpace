package peakspace.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import peakspace.validation.EmailValidation;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest{
        @EmailValidation
        String email;
        @NotNull
        String password;
}
