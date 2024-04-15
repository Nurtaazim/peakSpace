package peakspace.service;

import org.yaml.snakeyaml.scanner.ScannerImpl;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UserInfoResponse;

public interface UserInfoService {
    public SimpleResponse editProfile(UserInfoRequest userInfoRequest);

    public SimpleResponse saveAvg(UserInfoRequest userInfoRequest);
    public SimpleResponse saveHigher(UserInfoRequest userInfoRequest);

}
