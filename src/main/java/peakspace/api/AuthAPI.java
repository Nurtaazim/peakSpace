package peakspace.api;
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
    public SimpleResponse forgotPassword(@RequestParam("email") String email){
        return userService.forgot(email);
    }

    @PostMapping("/code")
    public SimpleResponse code(@RequestParam("code") int codeRequest) throws BadRequestException {
        return userService.randomCode(codeRequest);
    }

    @PostMapping("/newPassword")
    public UpdatePasswordResponse newPassword(@RequestBody PasswordRequest passwordRequest){
        return userService.updatePassword(passwordRequest);
    }
}
