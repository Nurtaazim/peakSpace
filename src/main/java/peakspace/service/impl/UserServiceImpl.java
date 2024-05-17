package peakspace.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peakspace.config.amazonS3.StorageService;
import peakspace.config.jwt.JwtService;
import peakspace.dto.request.*;
import peakspace.dto.response.*;
import peakspace.entities.*;
import peakspace.enums.Choise;
import peakspace.enums.Role;
import peakspace.exception.IllegalArgumentException;
import peakspace.exception.*;
import peakspace.repository.*;
import peakspace.repository.jdbsTamplate.SearchFriends;
import peakspace.service.ChapterService;
import peakspace.service.UserService;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final JwtService jwtService;
    private final ChapterRepository chapterRepository;
    private final PublicProfileRepository pablicProfileRepository;
    private final PublicationRepository publicationRepository;
    private final ProfileRepository profileRepository;
    private final SearchFriends searchFriends;
    private final ChapterService chapterService;
    private final StoryRepository storyRepository;
    private final StorageService storageService;
    private String userName;


    @Override
    @Transactional
    public ResponseWithGoogle verifyToken(String tokenFromGoogle) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(tokenFromGoogle);
            String email = decodedToken.getEmail();
            String phoneNumber = (String) decodedToken.getClaims().get("phone_number");
            boolean b = userRepository.existsByEmail(email);
            if (b) {
                User user = userRepository.getReferenceByEmail(email);
                return ResponseWithGoogle.builder()
                        .idUser(user.getId())
                        .token(jwtService.createToken(user))
                        .build();
            } else if (phoneNumber != null && phoneNumber.length() > 2) {
                String fullName = decodedToken.getName();
                User user = new User();
                Profile profile = new Profile();
                profile.setPhoneNumber(phoneNumber);
                String picture = decodedToken.getPicture();
                String defaultPassword = generatorDefaultPassword(8, 8);
                user.setRole(Role.USER);
                user.setPassword(passwordEncoder.encode(defaultPassword));
                user.setEmail(email);

                try {
                    sendConfirmationCode(email);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                user.setBlockAccount(true);
                profile.setAvatar(picture);
                profile.setUser(user);
                profileRepository.save(profile);
                userRepository.save(user);
                user.setProfile(profile);
                String[] parts = fullName.split(" ");
                if (parts.length >= 1) {
                    profile.setLastName(parts[0]);
                }
                if (parts.length >= 2) {
                    profile.setFirstName(parts[1]);
                }
                if (parts.length >= 3) {
                    profile.setPatronymicName(parts[2]);
                }
                String username = user.getProfile().getLastName();
                while (!user.getUsername().isEmpty()) {
                    if (!userRepository.existsByUserName(username)) {
                        user.setUserName(user.getProfile().getLastName().toLowerCase());
                    } else {
                        username = username + new Random().nextInt(1, userRepository.findAll().size() * 2);
                    }

                }
                return ResponseWithGoogle.builder()
                        .idUser(user.getId())
                        .description(email)
                        .build();
            }
            throw new NotActiveException();
        } catch (com.google.firebase.auth.FirebaseAuthException e) {
            throw new FirebaseAuthException();
        }
    }

    @Override
    @Transactional
    public ResponseWithGoogle signUpWithGoogle(RegisterWithGoogleRequest tokenFromGoogle) {
        User userForVerifier = userRepository.getReferenceById(tokenFromGoogle.idVerifierUser());
        if (userForVerifier.getPassword().equals(tokenFromGoogle.confirmationCode())) {
            User user = userRepository.getReferenceById(userForVerifier.getId());
            user.setIsBlock(false);
            sendDefaultPasswordToEmail(user);
            return ResponseWithGoogle.builder()
                    .idUser(user.getId())
                    .token(jwtService.createToken(user)).build();
        }
        throw new InvalidConfirmationCode();
    }

    @Override
    @Transactional
    public String sendConfirmationCode(String email) throws MessagingException {
        User user = userRepository.getByEmail(email);
        userName = user.getEmail();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("aliaskartemirbekov@gmail.com");
        mimeMessageHelper.setTo(email);
        String randomCode = generatorConfirmationCode(6);
        user.setConfirmationCode(randomCode);
        String fullName = user.getThisUserName();
        String message = "<!DOCTYPE html>\n" +
                         "<html lang=\"en\">\n" +
                         "\n" +
                         "<head>\n" +
                         "    <meta charset=\"UTF-8\">\n" +
                         "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                         "    <title>Confirmation Code</title>\n" +
                         "    <style>\n" +
                         "        body {\n" +
                         "            background-image: url('https://files.slack.com/files-pri/T023L1WBFLH-F06TT7FAU0J/img_2536.jpg');\n" +
                         "            background-size: cover;\n" +
                         "            background-position: center;\n" +
                         "            color: #ffffff;\n" +
                         "            font-family: Arial, sans-serif;\n" +
                         "            margin: 0;\n" +
                         "            padding: 0;\n" +
                         "        }\n" +
                         "\n" +
                         "        .container {\n" +
                         "            text-align: center;\n" +
                         "            padding: 10% 5%;\n" +
                         "        }\n" +
                         "\n" +
                         "        h2 {\n" +
                         "            color: #ffcc00;\n" +
                         "            font-size: 2.5em;\n" +
                         "            margin-bottom: 20px;\n" +
                         "        }\n" +
                         "\n" +
                         "        h3 {\n" +
                         "            color: #ff0000;\n" +
                         "            font-size: 2em;\n" +
                         "            margin-bottom: 15px;\n" +
                         "        }\n" +
                         "\n" +
                         "        p {\n" +
                         "            font-size: 1.2em;\n" +
                         "            margin-bottom: 10px;\n" +
                         "        }\n" +
                         "\n" +
                         "        @media (max-width: 768px) {\n" +
                         "            h2 {\n" +
                         "                font-size: 2em;\n" +
                         "            }\n" +
                         "\n" +
                         "            h3 {\n" +
                         "                font-size: 1.5em;\n" +
                         "            }\n" +
                         "\n" +
                         "            p {\n" +
                         "                font-size: 1em;\n" +
                         "            }\n" +
                         "\n" +
                         "            .container {\n" +
                         "                padding: 20% 5%;\n" +
                         "            }\n" +
                         "        }\n" +
                         "    </style>\n" +
                         "</head>\n" +
                         "\n" +
                         "<body>\n" +
                         "    <div class=\"container\">\n" +
                         "        <h2>Confirmation code!</h2>\n" +
                         "        <h2>ПРИВЕТ! " + fullName + "</h2>\n" +
                         "        <h3>Код подтверждения: " + randomCode + "</h3>\n" +
                         "        <p>НИКОМУ НЕ СООБЩАЙТЕ ЭТОТ КОД!</p>\n" +
                         "        <p>Это код для регистрации в Peak Space</p>\n" +
                         "        <p>Этот код действителен только 5 минут!</p>\n" +
                         "    </div>\n" +
                         "</body>\n" +
                         "\n" +
                         "</html>\n";
        mimeMessageHelper.setText(message, true);
        mimeMessageHelper.setSubject("Код Подтверждение!");
        javaMailSender.send(mimeMessage);
        return "Успешно отправленно код подтверждение на вашем емайл: " + email;
    }

    private String generatorConfirmationCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    private String generatorDefaultPassword(int minLength, int maxLength) {
        Random random = new Random();
        int length = +random.nextInt(maxLength - minLength + 1);
        StringBuilder sb = new StringBuilder(length);
        String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    private void sendDefaultPasswordToEmail(User user) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("arstanbeekovvv@gmail.com");
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setText("""
                                              Hi """ + user.getUsername() + """
                                                                                            
                                              """
                                      +
                                      user.getPassword()
                                      +
                                      """
                                                                                            
                                              НИКОМУ НЕ ГОВОРИТЕ КОД!
                                              Это пароль по умолчанию для Peakspace.
                                              Важно изменить этот пароль в целях вашей безопасности.
                                                                               
                                              Welcome to Peakspace!
                                              """);
            mimeMessageHelper.setSubject("Hello Kyrgyzstan !");
            javaMailSender.send(mimeMessage);
            System.out.println("Mail sent to " + user.getEmail());
        } catch (MessagingException e) {
            throw new NotActiveException();
        }
    }

    public SimpleResponse emailSender(String toEmail) throws MessagingException {
        String uuid = UUID.randomUUID().toString();
        User user = userRepository.getByEmail(toEmail);
        user.setConfirmationCode(uuid);
        userRepository.save(user);
        String link = "http://192.168.0.14:9090/createPassword.html?uuid=" + uuid;
        String htmlContent = String.format("""
                <html>
                <body>
                    <p>Для установки пароля, перейдите по следующей ссылке:</p>
                    <a href="%s">УСТАНОВИТЬ ПАРОЛЬ</a>
                </body>
                </html>
                """, link);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("PEAKSPACE");
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setText(uuid);
        mimeMessageHelper.setText(htmlContent, true);
        mimeMessageHelper.setSubject("Приложения PEAKSPACE!");
        javaMailSender.send(mimeMessage);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Успешно отправлен смс на почту !")
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse createPassword(String uuid, String password, String confirm) {
        if (!password.equals(confirm)) {
            throw new IllegalArgumentException(" Пароли не совпадают");
        } else {
            User user = userRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException("User not found"));
            user.setPassword(passwordEncoder.encode(password));
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Пароль успешно создан!")
                    .build();
        }
    }


    @Override
    @Transactional
    public SimpleResponse sendFriends(Long foundUserId, Long chapterId) {

        User currentUser = getCurrentUser();

        if (currentUser.getId().equals(foundUserId)) {
            throw new IllegalArgumentException("Нельзя подписаться на самого себя!");
        }

        Chapter targetChapter = null;
        for (Chapter chapter : currentUser.getChapters()) {
            if (chapter.getId().equals(chapterId)) {
                targetChapter = chapter;
                break;
            }
        }

        if (targetChapter == null) {
            throw new NotFoundException("Нет такого раздела: " + chapterId);
        }

        List<Chapter> userChapters = currentUser.getChapters();
        for (Chapter chapter : userChapters) {
            if (!chapter.getId().equals(chapterId) && chapter.getFriends().stream()
                    .anyMatch(user -> user.getId().equals(foundUserId))) {
                throw new IllegalArgumentException("Пользователь уже добавлен в другой раздел: " + chapter.getGroupName());
            }
        }

        List<User> updatedFriends = new ArrayList<>(targetChapter.getFriends());
        boolean removed = false;

        for (User friend : updatedFriends) {
            if (friend.getId().equals(foundUserId)) {
                updatedFriends.remove(friend);
                removed = true;
                break;
            }
        }

        User foundUser = userRepository.findById(foundUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + foundUserId + " не найден"));
        if (!removed) {

            updatedFriends.add(foundUser);
        }

        if (targetChapter.getId().equals(chapterId)) {
            targetChapter.setFriends(updatedFriends);
        } else throw new NotFoundException(" Нет такой раздел " + chapterId);


        Notification notification = new Notification();
        notification.setNotificationMessage("Подписался !");
        notification.setUserNotification(foundUser);
        notification.setSeen(false);
        notification.setCreatedAt(ZonedDateTime.now());
        notification.setSenderUserId(currentUser.getId());
        foundUser.getNotifications().add(notification);


        String message = removed ? "Удачно отписались!" : "Удачно подписались!";
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(message)
                .build();
    }

    @Override
    public List<SearchResponse> searchFriends(Choise sample, String keyWord) {
        getCurrentUser();
        if (sample.equals(Choise.User)) {
            return userRepository.findAllSearch(keyWord);
        } else if (sample.equals(Choise.Groups)) {
            return pablicProfileRepository.findAllPablic(keyWord);
        }
        throw new BadRequestException("Пллохой запрос !");

    }

    @Override
    @Transactional
    public SimpleResponse createChapter(ChapterRequest chapterRequest) {
        User currentUser = getCurrentUser();

        if (currentUser.getChapters().size() >= 5) {
            throw new BadRequestException("Превышено ограничение на количество раздел (максимум 5)!");
        }

        for (Chapter currentUserChapter : currentUser.getChapters()) {
            if (currentUserChapter.getGroupName().equals(chapterRequest.getGroupName())) {
                throw new IllegalArgumentException("У пользователя уже есть раздел с таким же названием!");
            }
        }

        Chapter chapter = new Chapter();
        chapter.setGroupName(chapterRequest.getGroupName());
        chapter.setUser(currentUser);
        chapterRepository.save(chapter);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Глава успешно сохранена!")
                .build();
    }

    @Override
    public List<SearchHashtagsResponse> searchHashtags(Choise sample, String keyword) {
        getCurrentUser();
        if (sample.equals(Choise.Hashtag)) {
            return publicationRepository.findAllHashtags(keyword);
        }
        throw new BadRequestException(" Плохой запрос !");
    }

    @Override
    public List<SearchResponse> searchMyFriends(Long chapterId, String userName) {
        getCurrentUser();
        Chapter chapter = chapterRepository.findByID(chapterId);
        if (chapter.getId().equals(chapterId)) {
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
    @Transactional
    public ProfileFriendsResponse findFriendsProfile(Long foundUserId) {

        User currentUser = getCurrentUser();
        ProfileFriendsResponse friendsResponse = userRepository.getId(foundUserId);

        User foundUser = userRepository.findById(foundUserId)
                .orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));

        List<Long> friends = currentUser.getSearchFriendsHistory();
        friends.add(foundUserId);

        int pablicationsSize = 0;
        if (foundUser.getPablicProfiles() != null && foundUser.getPablicProfiles().getUsers() != null) {
            pablicationsSize = foundUser.getPablicProfiles().getUsers().size();
        }

        return ProfileFriendsResponse.builder()
                .id(friendsResponse.getId())
                .avatar(friendsResponse.getAvatar())
                .cover(friendsResponse.getCover())
                .aboutYourSelf(friendsResponse.getAboutYourSelf())
                .profession(friendsResponse.getProfession())
                .friendsSize(foundUser.getChapters().size())
                .publicationsSize(pablicationsSize)
                .build();
    }

    @Override
    public List<ChapTerResponse> searchChapter(String search) {
        getCurrentUser();
        return userRepository.searchChapter(search);
    }

    @Override
    @Transactional
    public SimpleResponse unsubscribeUser(Long foundUserId) {
        User currentUser = getCurrentUser();
        User foundUser = userRepository.findByIds(foundUserId);
        boolean foundUserInFriends = false;

        for (Chapter chapter : currentUser.getChapters()) {
            List<User> friends = chapter.getFriends();
            if (friends.contains(foundUser)) {
                friends.remove(foundUser);
                foundUserInFriends = true;
            } else {
                friends.add(foundUser);
            }
            break;
        }
        String message = foundUserInFriends ? "Удачно отписано !" : " Удачно подписались !";

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(message)
                .build();
    }

    @Override
    public List<SubscriptionResponse> getAllSearchUserHistory() {
        User currentUser = getCurrentUser();
        List<Long> searchFriendsHistory = currentUser.getSearchFriendsHistory();
        List<SubscriptionResponse> searchUserResponses = new ArrayList<>();
        Set<Long> addedUserIds = new HashSet<>();

        for (int i = searchFriendsHistory.size() - 1; i >= 0; i--) {
            Long userId = searchFriendsHistory.get(i);

            if (!addedUserIds.contains(userId)) {
                User foundUser = userRepository.findById(userId).orElse(null);
                if (foundUser != null) {
                    SubscriptionResponse response = new SubscriptionResponse(
                            foundUser.getId(),
                            foundUser.getUsername(),
                            foundUser.getProfile().getAvatar(),
                            foundUser.getProfile().getAboutYourSelf()
                    );
                    searchUserResponses.add(response);
                    addedUserIds.add(userId);
                }
            }
        }

        return searchUserResponses;
    }

    @Override
    public List<SearchUserResponse> globalSearch(String keyWord) {
        List<SearchUserResponse> users = userRepository.findByAll("%" + keyWord + "%");
        return users.stream()
                .filter(user -> !getCurrentUser().getBlockAccounts().contains(user.getId()))
                .collect(Collectors.toList());
    }

    public FriendsPageResponse searchAllFriendsByChapter(Long userId, Long chapterId, String search) {
        return FriendsPageResponse.builder()
                .userId(userId)
                .chapters(chapterService.getAllChaptersByUserId(userId))
                .friendsResponsesList(searchFriends.getAllFriendsWithJDBCTemplate(userId, chapterId, search))
                .build();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Forbidden 403");
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) throws MessagingException {
        User user;
        if (signInRequest.email().endsWith("@gmail.com")) {
            user = userRepository.findByEmail(signInRequest.email()).orElseThrow(() -> new NotFoundException("User with this email not found!"));
        } else if (signInRequest.email().startsWith("+")) {
            Profile profile = profileRepository.findByPhoneNumber(signInRequest.email());
            if (profile == null)
                throw new NotFoundException("User with this phone number not found! " + signInRequest.email());
            user = profile.getUser();
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
    public SignUpResponse signUp(SignUpRequest signUpRequest) throws MessagingException {
        User user = new User();
        user.setUserName(signUpRequest.userName());
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setProfile(new Profile(signUpRequest.firstName(), signUpRequest.lastName(), user));
        user.setRole(Role.USER);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("arstanbeekovvv@gmail.com");
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
        return SignUpResponse.builder()
                .userId(user.getId())
                .message("Код подтверждения был отправлен на вашу почту.")
                .build();
    }

    @Override
    @Transactional
    public SignInResponse confirmToSignUp(int codeInEmail, long id) throws MessagingException {
        User user = userRepository.findById(id).orElseThrow(() -> new MessagingException("с таким айди пользователь не существует!"));
        if (user.getConfirmationCode().equals(String.valueOf(codeInEmail))) {
            user.setBlockAccount(false);
            user.setConfirmationCode(null);
            return SignInResponse.builder()
                    .id(user.getId())
                    .token(jwtService.createToken(user))
                    .build();
        } else throw new MessagingException("Не правильный код!");
    }

    public void startTask() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(this::yourMethod, 0, 1, TimeUnit.MINUTES);
    }

    private void yourMethod() {
        List<User> all = userRepository.findAll();
        for (User user1 : all) {
            if (ZonedDateTime.now().isAfter(user1.getCreatedAt().plusMinutes(3)) && user1.getBlockAccount()) {
                userRepository.delete(user1);
            }
        }
        List<Story> all1 = storyRepository.findAll();
        for (Story story : all1) {
            if (ZonedDateTime.now().isAfter(story.getCreatedAt().plusHours(24))) {
                for (Link_Publication linkPublication : story.getLinkPublications()) {
                    storageService.deleteFile(linkPublication.getLink());
                }
                storyRepository.delete(story);
            }
        }
    }
}

