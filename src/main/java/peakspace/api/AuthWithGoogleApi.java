package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.request.RegisterWithGoogleRequest;
import peakspace.dto.response.ResponseWithGoogle;
import peakspace.service.UserService;

@RestController("/auth-with-google")
@RequiredArgsConstructor
public class AuthWithGoogleApi {

    private final UserService userService;

    @PostMapping("/verify-token")
    @Operation(description = "аутентификация через google аккаунт.")
    public ResponseWithGoogle verifyToken(@RequestBody String tokenFromGoogle) {
        return userService.verifyToken(tokenFromGoogle);
    }

    @PostMapping("/sign-up-with-token")
    @Operation(description = "аутентификация и регистрация через google аккаунт.")
    public ResponseWithGoogle signUpWithGoogle(@RequestBody RegisterWithGoogleRequest registerWithGoogle) {
        return userService.signUpWithGoogle(registerWithGoogle);
    }

    @PostMapping("/send-again")
    @Operation(description = "отпровить код подтверждение для регистрация через google")
    public String sendConfCodeAgain(@RequestParam String email) throws MessagingException {
        return userService.sendConfirmationCode(email);
    }

}