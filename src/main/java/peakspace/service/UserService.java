package peakspace.service;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UpdatePasswordResponse;

public interface UserService {

    SimpleResponse forgot(String email) throws MessagingException;

    SimpleResponse randomCode(int codeRequest) throws BadRequestException, MessagingException;

    UpdatePasswordResponse updatePassword(PasswordRequest passwordRequest) throws MessagingException;
}
