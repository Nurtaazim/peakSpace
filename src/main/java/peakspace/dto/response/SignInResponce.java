package peakspace.dto.response;

import lombok.Builder;
import peakspace.validations.EmailValidation;

@Builder
public record SignInResponce(
        String token,
        Long id,
        String firstName,
         String lastName,
         String userName,
         String phoneNumber,
         String email,
         String password

        ) {
}
