package peakspace.service;

import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.UserInfoRespionse;

public interface UserInfoService {
     UserInfoRespionse editUserInfo(UserInfoRequest userInfoReqest);
}
