package peakspace.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.request.SignUpRequest;
import peakspace.dto.response.RegisterResponse;
import peakspace.dto.response.SignInRequest;
import peakspace.dto.response.SignInResponce;
import peakspace.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthAPI {
    private final UserService userService;
    @PostMapping("/signUp")
    public RegisterResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest){
    return userService.signUp(signUpRequest);
    }

    @PostMapping("signIn")
    public SignInResponce signIn(@RequestBody SignInRequest signInRequest){
       return userService.signIn(signInRequest);
    }
}
