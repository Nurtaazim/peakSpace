package peakspace.api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.request.RegisterWithGoogleRequest;
import peakspace.dto.response.ResponseWithGoogle;
import peakspace.service.UserService;

@RestController("/auth-with-google")
@RequiredArgsConstructor
public class AuthWithGoogleApi {

    private final UserService userService;

    @PostMapping("/verifyToken")
    public ResponseWithGoogle verifyToken(@RequestBody String tokenFromGoogle) {
        return userService.verifyToken(tokenFromGoogle);
    }

    @PostMapping("/sign_up-with_token")
    public ResponseWithGoogle signUpWithGoogle(@RequestBody RegisterWithGoogleRequest registerWithGoogle) {
        return userService.signUpWithGoogle(registerWithGoogle);
    }



}
