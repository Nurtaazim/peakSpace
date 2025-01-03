package peakspace.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peakspace.dto.response.AllFriendsResponse;
import peakspace.config.jwt.JwtService;
import peakspace.dto.request.*;
import peakspace.dto.response.*;
import peakspace.entities.*;
import peakspace.enums.Choise;
import peakspace.enums.Role;
import peakspace.exception.IllegalArgumentException;
import peakspace.exception.*;
import peakspace.exception.NotFoundException;
import peakspace.repository.*;
import peakspace.repository.jdbsTemplate.ChapterJdbcTemplate;
import peakspace.repository.jdbsTemplate.GetAllFriendsJdbcRepository;
import peakspace.repository.jdbsTemplate.SearchFriendsJdbcRepository;
import peakspace.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
    private final SearchFriendsJdbcRepository searchFriends;
    private final ChapterJdbcTemplate jdbcTemplate;
    private final GetAllFriendsJdbcRepository jdbcRepository;


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
                        .description(user.getUsername() + " успешно прошел аутентификацию!")
                        .token(jwtService.createToken(user))
                        .build();
            }
            String fullName = decodedToken.getName();
            User user = new User();
            Profile profile = new Profile();
            profile.setPhoneNumber(phoneNumber);
            String picture = decodedToken.getPicture();
            String defaultPassword = generatorDefaultPassword(8, 8);
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(defaultPassword));
            user.setEmail(email);
            user.setBlockAccount(true);
            profile.setAvatar(picture);
            profile.setUser(user);
            profileRepository.save(profile);
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
            user.setProfile(profile);
            if (user.getProfile().getAvatar() == null || user.getProfile().getAvatar().isEmpty())
                user.getProfile().setAvatar("https://img.myloview.com/stickers/default-avatar-profile-icon-vector-social-media-user-photo-700-205577532.jpg");
            if (user.getProfile().getCover() == null || user.getProfile().getAvatar().isEmpty())
                user.getProfile().setCover("https://bit.ly/3VK5PUn");
            userRepository.save(user);
            String username = user.getProfile().getLastName();
            while (user.getThisUserName() == null) {
                if (!userRepository.existsByUserName(username)) {
                    user.setUserName(user.getProfile().getLastName().toLowerCase());
                    break;
                } else {
                    username = username + new Random().nextInt(1, userRepository.findAll().size() * 2);
                }
            }
            try {
                sendConfirmationCode(email);
            } catch (MessagingException e) {
                throw new RuntimeException(e.getMessage());
            }
            return ResponseWithGoogle.builder()
                    .idUser(user.getId())
                    .description(email + " мы отправили код подтверждения на этот адрес электронной почты!")
                    .build();
        } catch (com.google.firebase.auth.FirebaseAuthException e) {
            throw new FirebaseAuthException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseWithGoogle signUpWithGoogle(RegisterWithGoogleRequest tokenFromGoogle) {
        User userForVerifier = userRepository.getReferenceById(tokenFromGoogle.idVerifierUser());
        if (userForVerifier.getConfirmationCode().equals(tokenFromGoogle.confirmationCode())) {
            User user = userRepository.getReferenceById(userForVerifier.getId());
            user.setIsBlock(false);
            user.setConfirmationCode(null);
//            user.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Bishkek")));
            sendDefaultPasswordToEmail(user);
            user.setBlockAccount(false);
            return ResponseWithGoogle.builder()
                    .idUser(user.getId())
                    .description("Вы успешно зарегистрировались! ")
                    .token(jwtService.createToken(user)).build();
        }
        throw new InvalidConfirmationCode("Confirmation code не совпадает! ");
    }

    @Override
    @Transactional
    public String sendConfirmationCode(String email) throws MessagingException {
        User user = userRepository.getByEmail(email);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom("nurtaazimmukanov24@gmail.com");
        mimeMessageHelper.setTo(email);
        String randomCode = generatorConfirmationCode();
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

    private String generatorConfirmationCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);
        String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 6; i++) {
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
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true,"UTF-8");
            mimeMessageHelper.setFrom("nurtaazimmukanov24@gmail.com");
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
            mimeMessageHelper.setSubject("Приложения PEAKSPACE!");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new SmsSendingException("Не удалось отправить электронное письмо на адрес " + user.getEmail() + ". Пожалуйста, проверьте адрес электронной почты и повторите попытку.");
        }
    }

    public SimpleResponse emailSender(String toEmail, String link) throws MessagingException {
        String uuid = UUID.randomUUID().toString();
        User user = userRepository.getByEmail(toEmail);
        user.setConfirmationCode(uuid);
        userRepository.save(user);
        String fullLinks = link + "/" + uuid;
        String htmlContent = String.format("""
                <html>
                <body>
                    <p>Для установки пароля, перейдите по следующей ссылке:</p>
                    <a href="%s">УСТАНОВИТЬ ПАРОЛЬ</a>
                </body>
                </html>
                """, fullLinks);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
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
    public SignInResponse createPassword(String uuid, String password, String confirm) {
        if (!password.equals(confirm)) {
            throw new IllegalArgumentException(" Пароли не совпадают");
        } else {
            User user = userRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException("User not found"));
            user.setPassword(passwordEncoder.encode(password));
            return SignInResponse.builder()
                    .id(user.getId())
                    .token(jwtService.createToken(user))
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

        Chapter targetChapter = getTargetChapter(currentUser, chapterId);

        if (jdbcTemplate.isFriendInOtherChapter(currentUser.getId(), foundUserId, chapterId)) {
            throw new IllegalArgumentException("Пользователь уже добавлен в другой раздел.");
        }

        boolean isRemoved = jdbcTemplate.removeFriendFromChapter(targetChapter.getId(), foundUserId);

        if (!isRemoved) {
            jdbcTemplate.addFriendToChapter(targetChapter.getId(), foundUserId);
            jdbcTemplate.sendNotification(currentUser.getId(), foundUserId);
        }

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(isRemoved ? "Удачно отписались!" : "Удачно подписались!")
                .build();
    }

    private Chapter getTargetChapter(User currentUser, Long chapterId) {
        return currentUser.getChapters().stream()
                .filter(chapter -> chapter.getId().equals(chapterId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Нет такого раздела: " + chapterId));
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
        if (sample.equals(Choise.Hashtag)) {
            return publicationRepository.findAllHashtags(keyword);
        }
        throw new BadRequestException(" Плохой запрос !");
    }

    @Override
    @Transactional
    public ProfileFriendsResponse findFriendsProfile(Long foundUserId) {

        User currentUser = getCurrentUser();
        ProfileFriendsResponse friendsResponse = userRepository.getId(foundUserId);

        User foundUser = userRepository.findById(foundUserId)
                .orElseThrow(() -> new NotFoundException(" Нет такого пользователя!"));

        int pablicationsSize = 0;
        if (foundUser.getCommunity().getUsers() != null) {
            pablicationsSize = foundUser.getCommunity().getUsers().size();
        }
        int sizeFriends = 0;
        for (Chapter chapter : currentUser.getChapters()) {
            sizeFriends = chapter.getFriends().size();
        }
        return ProfileFriendsResponse.builder()
                .id(friendsResponse.getId())
                .avatar(friendsResponse.getAvatar())
                .cover(friendsResponse.getCover())
                .userName(friendsResponse.getUserName())
                .aboutYourSelf(friendsResponse.getAboutYourSelf())
                .profession(friendsResponse.getProfession())
                .friendsSize(sizeFriends)
                .publicationsSize(pablicationsSize)
                .build();
    }

    @Override
    public List<ChapTerResponse> searchChapter(String search) {
        return userRepository.searchChapter(search);
    }

    @Override
    @Transactional
    public SimpleResponse unsubscribeUser(Long foundUserId) {
        User foundUser = userRepository.findByIds(foundUserId);
        boolean foundUserInFriends = false;

        for (Chapter chapter : getCurrentUser().getChapters()) {
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
                            foundUser.getThisUserName(),
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

    public List<FriendsResponse> searchAllFriendsByChapter(Long chapterId, String search) {
        return searchFriends.getAllFriendsWithJDBCTemplate(chapterId, search);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Доступ запрещен: у вас нет необходимых прав. Ошибка 403");
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) throws peakspace.exception.MessagingException {
        User user;
        if (signInRequest.email().endsWith("@gmail.com")) {
            user = userRepository.findByEmail(signInRequest.email()).orElseThrow(() -> new NotFoundException("Пользователь не найдено!"));
        } else if (signInRequest.email().startsWith("+")) {
            Profile profile = profileRepository.findByPhoneNumber(signInRequest.email());
            if (profile == null)
                throw new NotFoundException("Пользователь с таким номером телефона не найден! " + signInRequest.email());
            user = profile.getUser();
        } else {
            user = userRepository.getByUserName(signInRequest.email()).orElseThrow(() -> new NotFoundException("Пользователь не найдено!"));
        }
        if (user.getBlockAccount()) throw new NotFoundException("Учетная запись пользователя заблокирована! пожалуйста, свяжитесь со службой поддержки Peakspace! \nphone number: +996771234567 \nmail: peakspace@gmail.com ");
        if (passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            return SignInResponse.builder()
                    .id(user.getId())
                    .token(jwtService.createToken(user))
                    .build();
        } else throw new peakspace.exception.MessagingException("Неправильный пароль!");
    }

    @Override
    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) throws peakspace.exception.MessagingException, MessagingException {
        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new peakspace.exception.MessagingException("Пользователь с таким email уже существует!");
        }
        if (userRepository.existsByThisUserName(signUpRequest.userName())) {
            throw new peakspace.exception.MessagingException("Пользователь с таким user name уже существует!");
        }
        User user = new User();
        user.setUserName(signUpRequest.userName());
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setProfile(new Profile(signUpRequest.firstName(), signUpRequest.lastName(), user));
        user.setRole(Role.USER);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom("peakspace@gmail.com");
        mimeMessageHelper.setTo(signUpRequest.email());
        user.setConfirmationCode(String.valueOf(new Random().nextInt(1000, 9000)));
//        user.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Bishkek")));
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
        mimeMessageHelper.setSubject("Зарегистрируйтесь в PeakSpace");
        javaMailSender.send(mimeMessage);
        if (user.getProfile().getAvatar() == null)
            user.getProfile().setAvatar("https://img.myloview.com/stickers/default-avatar-profile-icon-vector-social-media-user-photo-700-205577532.jpg");
        if (user.getProfile().getCover() == null) user.getProfile().setCover("https://bit.ly/3VK5PUn");
        userRepository.save(user);
        return SignUpResponse.builder()
                .userId(user.getId())
                .message("Код подтверждения был отправлен на вашу почту.")
                .build();
    }

    @Override
    @Transactional
    public SignInResponse confirmToSignUp(int codeInEmail, long id) throws peakspace.exception.MessagingException {
        User user = userRepository.findById(id).orElseThrow(() -> new peakspace.exception.MessagingException("C таким айди пользователь не существует!"));
        if (user.getConfirmationCode().equals(String.valueOf(codeInEmail))) {
            user.setBlockAccount(false);
            user.setConfirmationCode(null);
            user.setIsBlock(false);
            return SignInResponse.builder()
                    .id(user.getId())
                    .token(jwtService.createToken(user))
                    .build();
        } else throw new peakspace.exception.MessagingException("Не правильный код!");
    }

    @Override
    public List<AllFriendsResponse> getAllFriendsById(Long userId, String userName) {
        return jdbcRepository.getAllFriendsById(userId, userName);
    }

    @Override
    @Transactional
    public SimpleResponse saveUserToHistorySearch(Long foundUserId) {
        List<Long> friendsHistory = getCurrentUser().getSearchFriendsHistory();
        if (friendsHistory == null) friendsHistory = new ArrayList<>();
        friendsHistory.remove(foundUserId);
        friendsHistory.add(foundUserId);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Пользователь успешно сохранен!")
                .build();
    }

    @Override
    @Transactional
    public void cancelConfirm(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким id не существует"));
        if (!user.getBlockAccount()) throw new ForbiddenException("Вы не можете удалить этого пользователя");
        userRepository.delete(user);
    }

    @PostConstruct
    public void init() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                    new ClassPathResource("google.json").getInputStream());
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials).build();
            FirebaseApp.initializeApp(firebaseOptions);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось инициализировать Firebase.");
        }
    }

    @Override
    public List<UserResponse> findAllUsers(Principal principal) {
        List<Long> blockAccounts = userRepository.getByEmail(principal.getName()).getBlockAccounts();
        return blockAccounts == null ?
                new ArrayList<>() :
                userRepository.findAllNotInWithIds(blockAccounts);
    }
}
