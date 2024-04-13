package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.UserInfoRespionse;
import peakspace.entities.Education;
import peakspace.entities.Profile;
import peakspace.entities.User;
import peakspace.enums.Role;
import peakspace.enums.Studies;
import peakspace.repository.ProfileRepository;
import peakspace.repository.UserRepository;
import peakspace.service.UserInfoService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class UserInfoServiceImpl implements UserInfoService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    @Override
    @Transactional
    @Modifying
    public UserInfoRespionse editUserInfo(UserInfoRequest userInfoReqest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);
        if(user.getRole().equals(Role.USER));

        Profile profile = profileRepository.findById(user.getProfile().getId()).get();
        profile.setAvatar(userInfoReqest.getAvatar());
        profile.setCover(userInfoReqest.getCover());
        user.setUserName(userInfoReqest.getUserName());
        profile.setFirstName(userInfoReqest.getFirstName());
        profile.setLastName(userInfoReqest.getLastName());
        profile.setAboutYourSelf(userInfoReqest.getAboutYourSelf());

        profile.setWorkOrNot(userInfoReqest.isWorkOrNot());


        List<Education> educations = user.getProfile().getEducations();
        for (Education education : educations) {

            if (userInfoReqest.getAvgAndHigher().equals(Studies.AVG)) {
                education.setAvgAndHigher(userInfoReqest.getAvgAndHigher());
                education.setEducationalInstitution(userInfoReqest.getEducationalInstitution());
                education.setCity(userInfoReqest.getCity());
            } else if (userInfoReqest.getAvgAndHigher().equals(Studies.HIGHER)) {
                education.setAvgAndHigher(userInfoReqest.getAvgAndHigher());
                education.setEducationalInstitution(userInfoReqest.getEducationalInstitution());
                education.setCity(userInfoReqest.getCity());
            }
        }

        for (Education education : educations) {
            return UserInfoRespionse.builder()
                    .id(user.getId())
                    .avatar(user.getProfile().getAvatar())
                    .cover(user.getProfile().getCover())
                    .userName(user.getUsername())
                    .firstName(user.getProfile().getFirstName())
                    .lastName(user.getProfile().getLastName())
                    .aboutYourSelf(user.getProfile().getAboutYourSelf())
                    .avgAndHigher(education.getAvgAndHigher())
                    .city(education.getCity())
                    .educationalInstitution(education.getEducationalInstitution())
                    .workOrNot(user.getProfile().isWorkOrNot())
                    .build();

        }
        return null;
    }

}
