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
import peakspace.validation.PasswordValidation;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthAPI {

    private final UserService userService;

    @Operation(summary = "Забыли пароль")
    @PutMapping("/forgotPassword")
    public SimpleResponse forgotPassword(@RequestParam String email,@RequestParam String link) throws MessagingException {
        return userService.emailSender(email,link);
    }

    @Operation(summary = " Cоздать новый пароль через ссылку который отправленный на его почту  !")
    @PostMapping("/createPassword")
    public SimpleResponse createPassword(@RequestParam @PasswordValidation String password,
                                         @RequestParam @PasswordValidation String confirm,
                                         @RequestParam String uuid) {
        return userService.createPassword(uuid, password, confirm);
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
    public SignInResponse confirm(@RequestParam int codeInEmail,
                                  long id) throws MessagingException {
        return userService.confirmToSignUp(codeInEmail, id);
    }

}