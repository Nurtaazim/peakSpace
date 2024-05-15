package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.request.SignInRequest;
import peakspace.dto.request.SignUpRequest;
import peakspace.dto.response.SignInResponse;
import peakspace.dto.response.SignUpResponse;
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
    @CrossOrigin(origins = "*", maxAge = 3600)
    public SimpleResponse forgotPassword(@RequestParam("email") String email) throws MessagingException {
        return userService.forgot(email);
    }

    @PostMapping("/code")
    @Operation(summary = " Сравнивание кода правильности !")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public SimpleResponse code(@RequestParam("code") int codeRequest, @RequestParam String email) throws BadRequestException {
        return userService.randomCode(codeRequest, email);
    }

    @PostMapping("/newPassword")
    @Operation(summary = " Изменение пароля  !")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public UpdatePasswordResponse newPassword(@RequestBody PasswordRequest passwordRequest, @RequestParam String email) {
        return userService.updatePassword(passwordRequest, email);
    }

    @PostMapping("/signIn")
    @Operation(summary = " Войти!")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public SignInResponse signIn(@RequestBody SignInRequest signInRequest) throws MessagingException {
        return userService.signIn(signInRequest);
    }

    @PostMapping("/signUp")
    @Operation(summary = " Регистрация!")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public SignUpResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest) throws MessagingException {
        return userService.signUp(signUpRequest);
    }

    @PostMapping("/confirmCodeByEmail")
    @CrossOrigin(origins = "*", maxAge = 3600)
    @Operation(summary = " Подтвердить регистрация через код!")
    public SignInResponse confirm(@RequestParam int codeInEmail, long id) throws MessagingException {
        return userService.confirmToSignUp(codeInEmail, id);
    }

}