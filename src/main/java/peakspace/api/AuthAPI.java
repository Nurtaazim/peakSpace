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

    @Secured("USER")
    @PostMapping("/forgot")
    @Operation(summary = "Отправление сообщение код  !")
    public SimpleResponse forgotPassword(@RequestParam("email") String email) throws MessagingException {
        return userService.forgot(email);
    }

    @Secured("USER")
    @PostMapping("/code")
    @Operation(summary = " Сравнивание кода правильности !")
    public SimpleResponse code(@RequestParam("code") int codeRequest) throws BadRequestException, MessagingException {
        return userService.randomCode(codeRequest);
    }

    @Secured("USER")
    @PostMapping("/newPassword")
    @Operation(summary = " Изменение пароля  !")
    public UpdatePasswordResponse newPassword(@RequestBody PasswordRequest passwordRequest) throws MessagingException {
        return userService.updatePassword(passwordRequest);
    }
    @GetMapping("/signIn")
    SignInResponse signIn (@Valid @RequestBody SignInRequest signInRequest) throws MessagingException {
        return userService.signIn(signInRequest);
    }

    @GetMapping("/signUp")
    String signUp (@Valid @RequestBody SignUpRequest signUpRequest) throws MessagingException {
        return userService.signUp(signUpRequest);
    }
    @PostMapping("/confirmCodeByEmail")
    SimpleResponse confirm(@RequestParam int codeInEmail, long id) throws MessagingException {
        return userService.confirmToSignUp(codeInEmail, id);
    }
}
