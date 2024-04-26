package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.request.AddEducationRequest;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.UserInfoService;

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

}
