package peakspace.api;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UpdatePasswordResponse;
import peakspace.service.UserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthAPI {

    private final UserService userService;

    @PostMapping("/forgot")
    @ApiOperation(value = "Отправление сообщение код  !")
    public SimpleResponse forgotPassword(@RequestParam("email") String email) throws MessagingException {
        return userService.forgot(email);
    }

    @PostMapping("/code")
    @ApiOperation(value = " Сравнивание кода правильности !")
    public SimpleResponse code(@RequestParam("code") int codeRequest) throws BadRequestException {
        return userService.randomCode(codeRequest);
    }

    @PostMapping("/newPassword")
    @ApiOperation(value = " Изменение пароля  !")
    public UpdatePasswordResponse newPassword(@RequestBody PasswordRequest passwordRequest) throws MessagingException {
        return userService.updatePassword(passwordRequest);
    }
}
