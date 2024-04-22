package peakspace.service;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.request.RegisterWithGoogleRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UpdatePasswordResponse;
import peakspace.dto.response.ResponseWithGoogle;

public interface UserService {

    ResponseWithGoogle verifyToken(String tokenFromGoogle);

    ResponseWithGoogle signUpWithGoogle(RegisterWithGoogleRequest tokenFromGoogle);

    SimpleResponse forgot(String email) throws MessagingException;

    SimpleResponse randomCode(int codeRequest) throws BadRequestException, MessagingException;

    UpdatePasswordResponse updatePassword(PasswordRequest passwordRequest) throws MessagingException;

    ResponseEntity<String> sendSms(String phoneNumberGetter, String verificationCode);
}
