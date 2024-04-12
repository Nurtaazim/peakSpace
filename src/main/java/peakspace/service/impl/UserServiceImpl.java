package peakspace.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peakspace.config.jwt.JwtService;
import peakspace.dto.request.SignUpRequest;
import peakspace.dto.response.*;
import peakspace.entities.Profile;
import peakspace.entities.User;
import peakspace.enums.Role;
import peakspace.repository.ProfileRepository;
import peakspace.repository.UserRepository;
import peakspace.service.UserService;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public RegisterResponse signUp(SignUpRequest signUpRequest) {
        boolean exists = userRepository.existsByEmail(signUpRequest.getEmail());
        if (exists) throw new RuntimeException("Email : " + signUpRequest.getEmail() + " already exist");

        Profile profile = new Profile();
        User user = new User();
        profile.setFirstName(signUpRequest.getFirstName());
        profile.setLastName(signUpRequest.getLastName());
        profile.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setUserName(signUpRequest.getUserName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setRole(Role.USER);

        profile.setUser(user);
        Profile profile1 = profileRepository.save(profile);
        user.setProfile(profile1);
        profile.setUser(user);
        userRepository.save(user);

        String token = jwtService.createToken(user);
        return RegisterResponse.builder()
                .token(token)
                .simpleResponse(SimpleResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Successfully saved")
                        .build())
                .build();
    }

    @Override
    public SignInResponce signIn(SignInRequest signInRequest) {
        User user = userRepository.findUserByAll(signInRequest.userInfo()).orElseThrow(() ->
                new NoSuchElementException("User with this info " + signInRequest.userInfo() + "not found!"));

        String encodePasEmail = user.getPassword();


        String password = signInRequest.password();

        boolean matches = passwordEncoder.matches(password, encodePasEmail);
        if(!matches) throw new RuntimeException("Invalid password");

        String token = jwtService.createToken(user);

            return SignInResponce.builder()
                    .token(token)
                    .id(user.getId())
                    .userName(user.getUsername())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .phoneNumber(user.getPhoneNumber())
                    .firstName(user.getProfile().getFirstName())
                    .lastName(user.getProfile().getLastName())
                    .build();


    }
}
