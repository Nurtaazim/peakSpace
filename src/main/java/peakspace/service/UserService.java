package peakspace.service;
import peakspace.dto.response.ResponseWithGoogle;

public interface UserService {

    ResponseWithGoogle verifyToken(String tokenFromGoogle);

    ResponseWithGoogle signUpWithGoogle(String tokenFromGoogle);

}
