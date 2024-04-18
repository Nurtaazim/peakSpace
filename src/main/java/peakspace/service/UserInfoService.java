package peakspace.service;

import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.SimpleResponse;

public interface UserInfoService {
    public SimpleResponse editProfile(UserInfoRequest userInfoRequest);



}
