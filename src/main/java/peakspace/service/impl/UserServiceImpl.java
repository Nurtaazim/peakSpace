package peakspace.service.impl;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UpdatePasswordResponse;
import peakspace.dto.response.SearchResponse;
import peakspace.dto.response.SearchHashtagsResponse;
import peakspace.dto.response.ProfileFriendsResponse;
import peakspace.entities.Chapter;
import peakspace.entities.PablicProfile;
import peakspace.entities.User;
import peakspace.enums.Role;
import peakspace.exception.BadRequestException;
import peakspace.exception.IllegalArgumentException;
import peakspace.exception.MessagingException;
import peakspace.exception.NotFoundException;
import peakspace.repository.ChapterRepository;
import peakspace.repository.PablicProfileRepository;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import peakspace.config.jwt.JwtService;

import peakspace.repository.ProfileRepository;
import peakspace.entities.User;
import peakspace.repository.UserRepository;
import peakspace.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.request.SignInRequest;
import peakspace.dto.request.SignUpRequest;
import peakspace.dto.response.SignInResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UpdatePasswordResponse;
import peakspace.entities.Profile;
import peakspace.enums.Role;
import peakspace.exception.MessagingException;
import peakspace.exception.NotFoundException;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final JwtService jwtService;
    private String userName;
    private int randomCode;
    private final ChapterRepository chapterRepository;
    private final PablicProfileRepository pablicProfileRepository;
    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;

    @Override
    public SimpleResponse forgot(String email) throws MessagingException, jakarta.mail.MessagingException {
        User user = userRepository.getByEmail(email);
        userName = user.getEmail();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("aliaskartemirbekov@gmail.com");
        mimeMessageHelper.setTo(email);
        Random random = new Random();
        randomCode = random.nextInt(9000) + 1000; // генерация случайного числа от 1000 до 9999
        String message = "<html>"
                + "<head>"
                + "<style>"
                + "body {"
                + "    background-image: url('https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png');"
                + "    background-size: cover;"
                + "    background-position: center;"
                + "    color: #ffffff;"
                + "    font-family: Arial, sans-serif;"
                + "}"
                + "h2 {"
                + "    color: #ffcc00;"
                + "}"
                + "h3 {"
                + "    color: #ff0000;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div style=\"text-align: center; padding: 50px;\">"
                + "<h2>Забыли пароль?</h2>"
                + "<p>Вы запросили сброс пароля для учетной записи на сайте. Ваш код подтверждения:</p>"
                + "<h3>Код подтверждения: " + randomCode + "</h3>"
                + "<p>Если это были не вы, просто проигнорируйте это сообщение.</p>"
                + "</div>"
                + "</body>"
                + "</html>";
        mimeMessageHelper.setText(message, true);
        mimeMessageHelper.setSubject("Забыли пароль?");
        javaMailSender.send(mimeMessage);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Код подтверждения был отправлен на вашу почту.")
                .build();
    }

    @Override
    public SimpleResponse randomCode(int codeRequest) throws MessagingException {
        if (randomCode != codeRequest) {
            throw new MessagingException("Не правильный код !!!");
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Код правильный !!!")
                .build();
    }

    @Override
    @Transactional
    public UpdatePasswordResponse updatePassword(PasswordRequest passwordRequest) throws MessagingException {
        if (!passwordRequest.getPassword().equals(passwordRequest.getConfirmPassword())) {
            throw new MessagingException("Пароль не корректный !");
        }
        User user = userRepository.getByEmail(userName);
        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        userRepository.save(user);
        return UpdatePasswordResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .token(jwtService.createToken(user))
                .build();
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) throws MessagingException {
        User user;
        if (signInRequest.email().endsWith("@gmail.com")) {
            user = userRepository.findByEmail(signInRequest.email()).orElseThrow(() -> new NotFoundException("User with this email not found!"));
        } else {
            user = userRepository.getByUserName(signInRequest.email()).orElseThrow(() -> new NotFoundException("Such user not found!"));
        }
        if (passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            return SignInResponse.builder()
                    .id(user.getId())
                    .token(jwtService.createToken(user))
                    .build();
        } else throw new MessagingException("Incorrect password!");
    }

    @Override
    public String signUp(SignUpRequest signUpRequest) throws jakarta.mail.MessagingException {
        User user = new User();
        user.setUserName(signUpRequest.userName());
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setProfile(new Profile(signUpRequest.name(), signUpRequest.surName(), user));
        user.setRole(Role.USER);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("aliaskartemirbekov@gmail.com");
        mimeMessageHelper.setTo(signUpRequest.email());
        user.setConfirmationCode(String.valueOf(new Random().nextInt(1000, 9000)));
        user.setCreatedAt(ZonedDateTime.now());
        user.setBlockAccount(true);
        String message = "<html>"
                + "<head>"
                + "<style>"
                + "body {"
                + "    background-image: url('https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png');"
                + "    background-size: cover;"
                + "    background-position: center;"
                + "    color: #ffffff;"
                + "    font-family: Arial, sans-serif;"
                + "}"
                + "h2 {"
                + "    color: #ffcc00;"
                + "}"
                + "h3 {"
                + "    color: #ff0000;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div style=\"text-align: center; padding: 50px;\">"
                + "<h2>Sign Up</h2>"
                + "<p>Ваш код подтверждения для регистрации:</p>"
                + "<h3>Код подтверждения: " + user.getConfirmationCode() + "</h3>"
                + "<p>Если это были не вы, просто проигнорируйте это сообщение.</p>"
                + "</div>"
                + "</body>"
                + "</html>";
        mimeMessageHelper.setText(message, true);
        mimeMessageHelper.setSubject("Sign Up to PeakSpace");
        javaMailSender.send(mimeMessage);
        userRepository.save(user);
        startTask();
        return "Код подтверждения был отправлен на вашу почту.";
    }

    @Override @Transactional
    public SimpleResponse confirmToSignUp(int codeInEmail, long id) throws MessagingException {
        User user = userRepository.findById(id).orElseThrow(() -> new MessagingException("\"Время истекло попробуйте снова!\""));
        if (user.getConfirmationCode().equals(String.valueOf(codeInEmail))) {
            user.setBlockAccount(false);
            user.setConfirmationCode(null);
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Вы успешно зарегистрировались!")
                        .build();
        }
        else throw new MessagingException("Не правильный код!");
    }
    public void startTask() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(this::yourMethod, 0, 1, TimeUnit.SECONDS);
    }

    private void yourMethod() {
        List<User> all = userRepository.findAll();
        for (User user1 : all) {
            if (ZonedDateTime.now().isAfter(user1.getCreatedAt().plusMinutes(3)) && user1.getBlockAccount()){
                userRepository.delete(user1);
            }
        }
    }


    @Override
    @Transactional
    public SimpleResponse sendFriends(Long foundUserId,String nameChapter) {

        User currentUser = getCurrentUser();

        if (currentUser.getId().equals(foundUserId)) {
            throw new IllegalArgumentException(" Нельзя подписатся самого себя !");
        }
        List<Chapter> chapters = currentUser.getChapters();
        boolean removed = false;
        String messages = "";

        for (Chapter chapter : chapters) {
            List<User> currentUserFriends = chapter.getFriends();
            List<User> updatedFriends = new ArrayList<>(currentUserFriends);

            for (User currentUserFriend : currentUserFriends) {
                if (currentUserFriend.getId().equals(foundUserId)) {
                    updatedFriends.remove(currentUserFriend);
                    removed = true;
                    break;
                }
            }

            if (!removed) {
                User foundUser = userRepository.findById(foundUserId).orElse(null);
                if (foundUser != null) {
                    updatedFriends.add(foundUser);
                }
            }

            if (chapter.getGroupName().equals(nameChapter)){
                chapter.setFriends(updatedFriends);
            }else throw new NotFoundException(" Нет такой раздел " + nameChapter);

            messages = (removed) ? "Удачно отписался!" : "Удачно подписались!";
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(messages)
                .build();
    }


    @Override
    public List<SearchResponse> searchFriends(String sample, String keyWord) {
        getCurrentUser();
        if (sample.equals("Пользователь")) {
            return userRepository.findAllSearch(keyWord);
        }
        if (sample.equals("Группы")) {
            return pablicProfileRepository.findAllPablic(keyWord);
        }
        throw new MessagingException("");
    }

    @Override @Transactional
    public SimpleResponse createChapter(ChapterRequest chapterRequest) {
        User currentUser = getCurrentUser();
        Chapter chapter = new Chapter();
        chapter.setGroupName(chapterRequest.getGroupName());
        chapterRepository.save(chapter);
        chapter.setUser(currentUser);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Удачно сохранился раздел !")
                .build();
    }
    @Override
    public List<SearchHashtagsResponse> searchHashtags(String keyword) {
        getCurrentUser();
        return publicationRepository.findAllHashtags(keyword);
    }

    @Override
    public List<SearchResponse> searchMyFriends(Long chapterId, String userName) {
        getCurrentUser();
        Chapter chapter = chapterRepository.findByID(chapterId);
        if (chapter.getId().equals(chapterId)){
            if (userName == null || userName.trim().isEmpty()) {
                return userRepository.findAllSearchEmpty();
            } else {
                return userRepository.findAllSearch(userName);
            }
        } else {
            throw new BadRequestException("Плохой запрос");
        }
    }

    @Override
    public ProfileFriendsResponse findFriendsProfile(Long foundUserId) {

        User currentUser = getCurrentUser();
        ProfileFriendsResponse friendsResponse = userRepository.getId(foundUserId);
        long friendSize = 0L;
        long pablicSize = 0L;
        User founUser = userRepository.findById(foundUserId).orElseThrow(() -> new NotFoundException("Нет такой пользователь !"));
        for (Chapter chapter : founUser.getChapters()) {
            friendSize +=getFriendsSize(chapter.getId());
        }

        for (PablicProfile pablicProfile : founUser.getPablicProfiles()) {
            pablicSize += getFriendsPublicSize(pablicProfile.getId());
        }

        List<PublicationResponse> friendsPublic = userRepository.findFriendsPublic(foundUserId);
        List<PublicationResponse> friendsFavorite = userRepository.findFavorite(foundUserId);
        List<PublicationResponse> friendTagWithMe = userRepository.findTagWithMe(foundUserId);

                 ProfileFriendsResponse response = ProfileFriendsResponse.builder()
                .id(friendsResponse.getId())
                .avatar(friendsResponse.getAvatar())
                .cover(friendsResponse.getCover())
                .aboutYourSelf(friendsResponse.getAboutYourSelf())
                .profession(friendsResponse.getProfession())
                .friendsSize(friendSize)
                .pablicationsSize(pablicSize)
                .friendsPublications(friendsPublic)
                .friendsFavoritesPublications(friendsFavorite)
                .friendsWitMePublications(friendTagWithMe)
                .build();
                 return response;
    }

    private long getFriendsSize(Long foundUserID){
        Chapter chapter = chapterRepository.findByID(foundUserID);
        return chapter.getFriends().size();
    }
    private long getFriendsPublicSize(Long foundUserID){
        PablicProfile pablicProfile = pablicProfileRepository.findByIds(foundUserID);
        return pablicProfile.getPublications().size();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Forbidden 403");
    }
}


