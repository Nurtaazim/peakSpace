package peakspace.dto.request;

import lombok.Getter;
import lombok.Setter;
import peakspace.enums.Role;
import peakspace.validations.EmailValidation;
import peakspace.validations.PasswordValidation;
import peakspace.validations.PhoneNumberValidation;

@Getter @Setter
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String userName;
    @PhoneNumberValidation
    private String phoneNumber;
    private Role role;
    @EmailValidation
    private String email;
    @PasswordValidation
    private String password;
}
