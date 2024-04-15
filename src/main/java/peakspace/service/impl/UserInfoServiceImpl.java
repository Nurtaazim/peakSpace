package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.entities.Education;
import peakspace.entities.Profile;
import peakspace.entities.User;
import peakspace.enums.Studies;
import peakspace.repository.EducationRepository;
import peakspace.repository.ProfileRepository;
import peakspace.repository.UserRepository;
import peakspace.service.UserInfoService;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;
    private final EducationRepository educationRepo;
    private final ProfileRepository profileRepo;

    @Transactional
    @Override
    public SimpleResponse editProfile(UserInfoRequest userInfoRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        user.getProfile().setAvatar(userInfoRequest.getAvatar());
        user.getProfile().setCover(userInfoRequest.getCover());
        user.setUserName(userInfoRequest.getUserName());
        user.getProfile().setFirstName(userInfoRequest.getFirstName());
        user.getProfile().setLastName(userInfoRequest.getLastName());
        user.getProfile().setAboutYourSelf(userInfoRequest.getAboutYourSelf());
        user.getProfile().setProfession(userInfoRequest.getProfession());
        user.getProfile().setWorkOrNot(userInfoRequest.isWorkOrNot());

        if (userInfoRequest.getAvgAndHigher() != null) {
            Education education = new Education();
            Profile profile = userRepository.findBYProfile(user.getProfile().getId());
            if (userInfoRequest.getAvgAndHigher().equals(Studies.AVG)) {
                education.setAvgAndHigher(userInfoRequest.getAvgAndHigher());
                education.setCity(userInfoRequest.getCity());
                education.setEducationalInstitution(userInfoRequest.getEducationalInstitution());
                education.setProfile(profile);
                educationRepo.save(education);
                profile.getEducations().add(education);
                profileRepo.save(profile);
                user.setProfile(profile);

            } else if (userInfoRequest.getAvgAndHigher().equals(Studies.HIGHER)) {
                Education education2 = new Education();
                education2.setAvgAndHigher(userInfoRequest.getAvgAndHigher());
                education2.setCity(userInfoRequest.getCity());
                education2.setEducationalInstitution(userInfoRequest.getEducationalInstitution());
                education2.setProfile(profile);
                educationRepo.save(education2);
                profile.getEducations().add(education2);
                profileRepo.save(profile);
                user.setProfile(profile);
            }
        }
        userRepository.save(user);
        return SimpleResponse.builder()
                .message("Successfully  saved!")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    //-------------------------------------------------------------------------------
    //Тут второй вариант, по отдельности
    //------------------------------------------------------------------------------------
    @Override
    public SimpleResponse saveAvg(UserInfoRequest userInfoRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        Education education = new Education();
        Profile profile = userRepository.findBYProfile(user.getProfile().getId());
        if (userInfoRequest.getAvgAndHigher().equals(Studies.AVG)) {
            education.setAvgAndHigher(userInfoRequest.getAvgAndHigher());
            education.setCity(userInfoRequest.getCity());
            education.setEducationalInstitution(userInfoRequest.getEducationalInstitution());
            education.setProfile(profile);
            educationRepo.save(education);
            profile.getEducations().add(education);
            profileRepo.save(profile);
            user.setProfile(profile);
        }
        userRepository.save(user);
        return SimpleResponse.builder()
                .message("Successfully  saved!")
                .httpStatus(HttpStatus.OK)
                .build();

    }

    @Override
    public SimpleResponse saveHigher(UserInfoRequest userInfoRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        Profile profile = userRepository.findBYProfile(user.getProfile().getId());
        if (userInfoRequest.getAvgAndHigher().equals(Studies.HIGHER)) {
            Education education2 = new Education();
            education2.setAvgAndHigher(userInfoRequest.getAvgAndHigher());
            education2.setCity(userInfoRequest.getCity());
            education2.setEducationalInstitution(userInfoRequest.getEducationalInstitution());
            education2.setProfile(profile);
            educationRepo.save(education2);
            profile.getEducations().add(education2);
            profileRepo.save(profile);
            user.setProfile(profile);
        }
        userRepository.save(user);
        return SimpleResponse.builder()
                .message("Successfully  saved!")
                .httpStatus(HttpStatus.OK)
                .build();
    }

}
