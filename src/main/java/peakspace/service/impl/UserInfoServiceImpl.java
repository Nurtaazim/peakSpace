package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peakspace.dto.request.AddEducationRequest;
import peakspace.dto.request.UserInfoRequest;
import peakspace.dto.response.BlockAccountsResponse;
import peakspace.dto.response.EducationResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UserInfoResponse;
import peakspace.entities.Education;
import peakspace.entities.Profile;
import peakspace.entities.User;
import peakspace.enums.Country;
import peakspace.exception.NotFoundException;
import peakspace.repository.EducationRepository;
import peakspace.repository.ProfileRepository;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.service.UserInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        user.getProfile().setPatronymicName(userInfoRequest.getPatronymicName());
        user.getProfile().setAboutYourSelf(userInfoRequest.getAboutYourSelf());
        user.getProfile().setProfession(userInfoRequest.getProfession());
        user.getProfile().setWorkOrNot(userInfoRequest.isWorkOrNot());
        user.getProfile().setLocation(userInfoRequest.getLocation());
        Profile profile = userRepository.findBYProfile(user.getProfile().getId());
        profileRepo.save(profile);
        user.setProfile(profile);

        userRepository.save(user);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Удачно сохранено!")
                .build();

    }

    @Transactional
    @Override
    public SimpleResponse addEducation(AddEducationRequest addEducationRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        Profile profile = user.getProfile();

        boolean educationExists = profile.getEducations().stream()
                .anyMatch(education ->
                        education.getCountry().equals(addEducationRequest.getCountry()) &&
                                education.getEducationalInstitution().equals(addEducationRequest.getEducationalInstitution()));

        if (educationExists) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(" Образование для этого профиля уже существует !")
                    .build();
        }

        Education education = new Education();
        education.setCountry(addEducationRequest.getCountry());
        education.setEducationalInstitution(addEducationRequest.getEducationalInstitution());
        education.setProfile(profile);

        educationRepo.save(education);
        profile.getEducations().add(education);
        profileRepo.save(profile);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Удачно сохранилось образование!")
                .build();
    }



    @Transactional
    @Override
    public SimpleResponse blockAccount(Long userId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        User foundUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));

        if (!user.getId().equals(foundUser.getId())) {
         if (user.getBlockAccounts().contains(foundUser.getId())) {
            user.getBlockAccounts().remove(foundUser.getId());
            foundUser.setBlockAccount(false);
         } else {
            user.getBlockAccounts().add(foundUser.getId());
            foundUser.setBlockAccount(true);
         }
    }else {
            System.out.println("Вы не можете заблокировать себя");
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Удачно блокировано!")
                .build();
    }

    @Override
    public List<BlockAccountsResponse> getBlockAccounts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);

        List<BlockAccountsResponse> accounts = new ArrayList<>();
        List<Long> blockAccounts = user.getBlockAccounts();

        for (Long blockAccount : blockAccounts) {
            User user1 = userRepository.getReferenceById(blockAccount);
            accounts.add(BlockAccountsResponse.builder()
                    .userName(user1.getThisUserName())
                    .avatar(user1.getProfile().getAvatar())
                    .cover(user1.getProfile().getCover())
                    .aboutYourSelf(user1.getProfile().getAboutYourSelf())
                    .firstName(user1.getProfile().getFirstName())
                    .lastName(user1.getProfile().getLastName())
                    .build());
        }
        return accounts;
    }

    @Transactional
    @Override
    public SimpleResponse closeAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);
        user.setIsBlock(!user.getIsBlock());
        if (user.getIsBlock()) {
            return SimpleResponse.builder()
                    .message(" Успешно вернулись в закрытый аккаунт!")
                    .httpStatus(HttpStatus.OK)
                    .build();
        }
        else return SimpleResponse.builder()
                .message(" Успешно вернулись в открытый аккаунт!")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public SimpleResponse deleteEducation(Long eduId) {
        educationRepo.deleteEducation(eduId);
        return SimpleResponse.builder()
                .message("Удачно удалено образование !")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public UserInfoResponse getUserInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);
        Profile profile = user.getProfile();

        List<EducationResponse> educationResponses = profile.getEducations().stream()
                .map(this::mapToEducationResponse)
                .collect(Collectors.toList());

        String fullName = String.format("%s %s", profile.getFirstName(), profile.getLastName());

        return UserInfoResponse.builder()
                .avatar(profile.getAvatar())
                .cover(profile.getCover())
                .userName(profile.getUser().getUsername())
                .fullName(fullName)
                .aboutYourSelf(profile.getAboutYourSelf())
                .educationResponses(educationResponses)
                .profession(profile.getProfession())
                .build();
    }

    private EducationResponse mapToEducationResponse(Education education) {
        return EducationResponse.builder()
                .id(education.getId())
                .country(education.getCountry())
                .educationalInstitution(education.getEducationalInstitution())
                .build();
    }
}