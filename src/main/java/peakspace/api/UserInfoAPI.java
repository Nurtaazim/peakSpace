package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.AddEducationRequest;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.BlockAccountsResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UserInfoResponse;
import peakspace.service.UserInfoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-infos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserInfoAPI {

    private final UserInfoService userInfoService;

    @Secured("USER")
    @Operation(summary = " Заполнение профиля текущего пользователья !")
    @PutMapping
    public SimpleResponse edit(@RequestBody @Valid UserInfoRequest userInfoRequest) {
        return userInfoService.editProfile(userInfoRequest);
    }

    @Secured("USER")
    @Operation(summary = " Заполнение образование !")
    @PostMapping
    public SimpleResponse addEducation(@RequestBody AddEducationRequest addEducationRequest) {
        return userInfoService.addEducation(addEducationRequest);
    }

    @Secured("USER")
    @Operation(summary = " Удаление образование !")
    @DeleteMapping("/{eduId}")
    public SimpleResponse deleteEducation(@PathVariable Long eduId){
        return userInfoService.deleteEducation(eduId);
    }

    @Secured("USER")
    @Operation(summary = " Для получения страница UserInfo ")
    @GetMapping()
    public UserInfoResponse getUserInfo(){
        return userInfoService.getUserInfo();
    }

    @Secured("USER")
    @Operation(summary = "Заблокирование аккаунта!")
    @PutMapping("/block/{userId}")
    public SimpleResponse blockAccount(@PathVariable Long userId) {
        return userInfoService.blockAccount(userId);
    }

    @Secured("USER")
    @Operation(summary = " Получить заблокирование аккаунты!")
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
