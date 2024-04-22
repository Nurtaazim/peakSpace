package peakspace.dto.request;

import lombok.Builder;
import peakspace.validation.EmailValidation;

@Builder
public record SignInRequest(
        @EmailValidation
        String email,
        String password
) {
}
