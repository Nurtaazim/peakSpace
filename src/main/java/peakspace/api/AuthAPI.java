package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.annotation.Secured;
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
    @Operation(summary = "Отправление сообщение код  !")
    public SimpleResponse forgotPassword(@RequestParam("email") String email) throws MessagingException {
        return userService.forgot(email);
    }



    @PostMapping("/code")
    @Operation(summary = " Сравнивание кода правильности !")
    public SimpleResponse code(@RequestParam("code") int codeRequest, @RequestParam String email) throws BadRequestException {
        return userService.randomCode(codeRequest, email);
    }


    @PostMapping("/newPassword")
    @Operation(summary = " Изменение пароля  !")
    public UpdatePasswordResponse newPassword(@RequestBody PasswordRequest passwordRequest, @RequestParam String email) {
        return userService.updatePassword(passwordRequest, email);
    }

    @GetMapping("/signIn")
    SignInResponse signIn( @RequestBody @Valid SignInRequest signInRequest) throws MessagingException {
        return userService.signIn(signInRequest);
    }

    @GetMapping("/signUp")
    String signUp( @RequestBody @Valid SignUpRequest signUpRequest) throws MessagingException {
        return userService.signUp(signUpRequest);
    }

    @PostMapping("/confirmCodeByEmail")
    SimpleResponse confirm(@RequestParam int codeInEmail, long id) throws MessagingException {
        return userService.confirmToSignUp(codeInEmail, id);
    }
}
