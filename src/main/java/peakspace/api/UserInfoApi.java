package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.AddEducationRequest;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.BlockAccountsResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.UserInfoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserInfoApi {

    private final UserInfoService userInfoService;

    @Secured("USER")
    @Operation(summary = " Заполнение профиля текущего пользователья !")
    @PostMapping("/userInfo")
    public SimpleResponse edit(@RequestBody UserInfoRequest userInfoRequest) {
        return userInfoService.editProfile(userInfoRequest);
    }

    @Secured("USER")
    @Operation(summary = " Заполнение образование !")
    @PostMapping("/addEdu")
    public SimpleResponse addEducation(@RequestBody AddEducationRequest addEducationRequest) {
        return userInfoService.addEducation(addEducationRequest);
    }
    @Secured("USER")
    @Operation(summary = "Заблокирование аккаунта!")
    @PostMapping("block/{userId}")
    public SimpleResponse blockAccount(@PathVariable Long userId){
        return userInfoService.blockAccount(userId);
    }
    @Secured("USER")
    @Operation(summary = " Get заблокирование аккаунты!")
    @PostMapping("getBlockAccounts")
    public List<BlockAccountsResponse> getBlockAccounts(){
        return userInfoService.getBlockAccounts();
    }

    @Secured("USER")
    @Operation(summary = " Сделать закрытый аккаунт!")
    @PostMapping("closeAccount")
    public SimpleResponse closeAccount(){
        return userInfoService.closeAccount();
    }

}
