package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import peakspace.dto.request.AddEducationRequest;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.BlockAccountsResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.UserInfoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-infos")
public class UserInfoAPI {

    private final UserInfoService userInfoService;

    @Secured("USER")
    @Operation(summary = " Заполнение профиля текущего пользователья !")
    @PutMapping
    public SimpleResponse edit(@RequestBody UserInfoRequest userInfoRequest) {
        return userInfoService.editProfile(userInfoRequest);
    }

    @Secured("USER")
    @Operation(summary = " Заполнение образование !")
    @PostMapping
    public SimpleResponse addEducation(@RequestBody AddEducationRequest addEducationRequest) {
        return userInfoService.addEducation(addEducationRequest);
    }

    @Secured("USER")
    @Operation(summary = "Заблокирование аккаунта!")
    @PutMapping("/block/{userId}")
    public SimpleResponse blockAccount(@PathVariable Long userId) {
        return userInfoService.blockAccount(userId);
    }

    @Secured("USER")
    @Operation(summary = " Get заблокирование аккаунты!")
    @GetMapping("/block-accounts")
    public List<BlockAccountsResponse> getBlockAccounts() {
        return userInfoService.getBlockAccounts();
    }

    @Secured("USER")
    @Operation(summary = " Сделать закрытый аккаунт!")
    @PostMapping("/close-account")
    public SimpleResponse closeAccount() {
        return userInfoService.closeAccount();
    }

}
