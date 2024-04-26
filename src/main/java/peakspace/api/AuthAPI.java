package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import peakspace.dto.request.PasswordRequest;
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
}
