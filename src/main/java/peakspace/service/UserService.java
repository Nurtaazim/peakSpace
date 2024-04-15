package peakspace.service;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.request.SignInRequest;
import peakspace.dto.request.SignUpRequest;
import peakspace.dto.response.SignInResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UpdatePasswordResponse;

public interface UserService {
    SimpleResponse forgot(String email) throws MessagingException;

    SimpleResponse randomCode(int codeRequest) throws BadRequestException;

    UpdatePasswordResponse updatePassword(PasswordRequest passwordRequest) throws MessagingException;

    SignInResponse signIn(SignInRequest signInRequest);

    String signUp(SignUpRequest signUpRequest) throws MessagingException;

    SimpleResponse confirmToSignUp(int codeInEmail);
}
