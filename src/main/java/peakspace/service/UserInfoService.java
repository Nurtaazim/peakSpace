package peakspace.service;

import peakspace.dto.request.AddEducationRequest;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.SimpleResponse;

public interface UserInfoService {
    public SimpleResponse editProfile(UserInfoRequest userInfoRequest);

    public SimpleResponse addEducation(AddEducationRequest addEducationRequest);

}
