package peakspace.service;

import peakspace.dto.request.AddEducationRequest;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.SimpleResponse;

public interface UserInfoService {
     SimpleResponse editProfile(UserInfoRequest userInfoRequest);

     SimpleResponse addEducation(AddEducationRequest addEducationRequest);

}
