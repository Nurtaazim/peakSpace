package peakspace.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.request.AddEducationRequest;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UserInfoResponse;
import peakspace.service.UserInfoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserInfoApi {

    private final UserInfoService userInfoService;

    @PostMapping("/userInfo")
    public SimpleResponse edit(@RequestBody UserInfoRequest userInfoRequest){
        return userInfoService.editProfile(userInfoRequest);
    }

    @PostMapping("/addEdu")
    public SimpleResponse addEducation(@RequestBody AddEducationRequest addEducationRequest){
        return userInfoService.addEducation(addEducationRequest);

    }

}
