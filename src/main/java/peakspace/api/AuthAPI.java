package peakspace.api;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.request.SignInRequest;
import peakspace.dto.request.SignUpRequest;
import peakspace.dto.response.SignInResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UpdatePasswordResponse;
import peakspace.service.UserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthAPI {

    private final UserService userService;


    @PostMapping("/forgot")
    public SimpleResponse forgotPassword(@RequestParam("email") String email) throws MessagingException {
        return userService.forgot(email);
    }

    @PostMapping("/code")
    public SimpleResponse code(@RequestParam("code") int codeRequest) throws BadRequestException {
        return userService.randomCode(codeRequest);
    }

    @PostMapping("/newPassword")
    public UpdatePasswordResponse newPassword(@RequestBody PasswordRequest passwordRequest) throws MessagingException {
        return userService.updatePassword(passwordRequest);
    }
    @GetMapping("/signIn")
    SignInResponse signIn (@RequestBody SignInRequest signInRequest){
        return userService.signIn(signInRequest);
    }
    @GetMapping("/signUp")
    String signUp (@RequestBody SignUpRequest signUpRequest) throws MessagingException {
        return userService.signUp(signUpRequest);
    }
    @PostMapping("/confirmCodeByEmail")
    SimpleResponse confirm(@RequestParam int codeInEmail){
        return userService.confirmToSignUp(codeInEmail);
    }
}
