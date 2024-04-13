package peakspace.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.UserInfoRespionse;
import peakspace.service.UserInfoService;
import peakspace.service.impl.UserInfoServiceImpl;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/userInfo")
public class UserInfoApi {

    private final UserInfoService userInfoService;

    @Secured("USER")
    @PostMapping("/userInfo")
    public UserInfoRespionse editProfile(@RequestBody UserInfoRequest userInfoRequest){
        return userInfoService.editUserInfo(userInfoRequest);
    }
}
