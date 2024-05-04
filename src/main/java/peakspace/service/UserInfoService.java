package peakspace.service;

import peakspace.dto.request.AddEducationRequest;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.BlockAccountsResponse;
import peakspace.dto.response.GetAllPostsResponse;
import peakspace.dto.response.SearchResponse;
import peakspace.dto.response.SimpleResponse;

import java.util.List;

public interface UserInfoService {
     SimpleResponse editProfile(UserInfoRequest userInfoRequest);
     SimpleResponse addEducation(AddEducationRequest addEducationRequest);
     SimpleResponse blockAccount(Long userId);
     List<BlockAccountsResponse> getBlockAccounts();
     SimpleResponse closeAccount();

}
