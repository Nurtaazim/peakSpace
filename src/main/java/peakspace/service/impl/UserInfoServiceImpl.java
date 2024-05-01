package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peakspace.dto.request.AddEducationRequest;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.BlockAccountsResponse;
import peakspace.dto.response.GetAllPostsResponse;
import peakspace.dto.response.SearchResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.entities.Education;
import peakspace.entities.Profile;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.exception.NotFoundException;
import peakspace.repository.EducationRepository;
import peakspace.repository.ProfileRepository;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.service.UserInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;
    private final EducationRepository educationRepo;
    private final ProfileRepository profileRepo;
    private final PublicationRepository publicationRepo;

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

        Education education = new Education();
        Profile profile = userRepository.findBYProfile(user.getProfile().getId());

        education.setCountry(userInfoRequest.getCountry());
        education.setLocation(userInfoRequest.getLocation());
        education.setEducationalInstitution(userInfoRequest.getEducationalInstitution());


        education.setProfile(profile);
        educationRepo.save(education);
        profile.getEducations().add(education);
        profileRepo.save(profile);
        user.setProfile(profile);

        userRepository.save(user);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully saved!")
                .build();

    }

    @Override
    public SimpleResponse addEducation(AddEducationRequest addEducationRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        Education education = new Education();
        Profile profile = userRepository.findBYProfile(user.getProfile().getId());

        education.setCountry(addEducationRequest.getCountry());
        education.setEducationalInstitution(addEducationRequest.getEducationalInstitution());

        education.setProfile(profile);
        educationRepo.save(education);
        profile.getEducations().add(education);
        profileRepo.save(profile);
        user.setProfile(profile);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully saved!")
                .build();
    }

    @Override
    public SimpleResponse blockAccount(Long userId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        User foundUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        if(user.getBlockAccounts().contains(foundUser.getId())){
            user.getBlockAccounts().remove(foundUser.getId());
            foundUser.setBlockAccount(false);
        }else {
            user.getBlockAccounts().add(foundUser.getId());
            foundUser.setBlockAccount(true);
        }
        return null;
    }

    @Override
    public List<BlockAccountsResponse> getBlockAccounts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        List<BlockAccountsResponse> accounts = new ArrayList<>();
        List<Long> blockAccounts = user.getBlockAccounts();

        for (Long blockAccount : blockAccounts) {
            User user1 = userRepository.getReferenceById(blockAccount);

        }
      return null;
    }

    @Override
    public SimpleResponse closeAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);
        if(user.getIsBlock().equals(false)){
            user.setBlockAccount(true);
        }else{
            user.setIsBlock(false);
        }
        return SimpleResponse.builder()
                .message("Successfully saved!")
                .httpStatus(HttpStatus.OK)
                .build();
    }
}