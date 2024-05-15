package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.request.SignInRequest;
import peakspace.dto.request.SignUpRequest;
import peakspace.dto.response.SignInResponse;
import peakspace.dto.response.SignUpResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UpdatePasswordResponse;
import peakspace.service.UserService;

@RestController
@RequestMapping("/auth")
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

    @PostMapping("/signIn")
    @Operation(summary = " Войти!")
    public SignInResponse signIn(@RequestBody SignInRequest signInRequest) throws MessagingException {
        return userService.signIn(signInRequest);
    }

    @PostMapping("/signUp")
    @Operation(summary = " Регистрация!")
    public SignUpResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest) throws MessagingException {
        return userService.signUp(signUpRequest);
    }

    @PostMapping("/confirmCodeByEmail")
    @Operation(summary = " Подтвердить регистрация через код!")
    public SignInResponse confirm(@RequestParam int codeInEmail, long id) throws MessagingException {
        return userService.confirmToSignUp(codeInEmail, id);
    }

}