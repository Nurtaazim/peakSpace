package peakspace.service.impl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.web.client.RestTemplate;
import peakspace.config.jwt.JwtService;
import peakspace.dto.request.RegisterWithGoogleRequest;
import peakspace.dto.response.ResponseWithGoogle;
import peakspace.entities.Profile;
import peakspace.exception.InvalidConfirmationCode;
import peakspace.exception.SmsSendingException;
import peakspace.repository.ProfileRepository;
import peakspace.entities.User;
import peakspace.enums.Role;
import peakspace.exception.FirebaseAuthException;
import peakspace.exception.NotActiveException;
import peakspace.repository.UserRepository;
import peakspace.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UpdatePasswordResponse;
import java.util.Locale;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final JwtService jwtService;
    private String userName;
    private int randomCode;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    @Value("${http://smspro.nikita.kg/api/message}")
    private String apiURL;
    @Value("${bfc87413578d8bf8181838ca5418239b}")
    private String apiKey;

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
                        .id(user.getId())
                        .token(jwtService.createToken(user))
                        .build();
            }
            else if (phoneNumber != null && phoneNumber.length() > 2) {
                String fullName = decodedToken.getName();
                User user = new User();
                Profile profile = new Profile();
                String picture = decodedToken.getPicture();
                String defaultPassword = userRepository.generatorDefaultPassword(8, 8);
                user.setRole(Role.USER);
                user.setPassword(passwordEncoder.encode(defaultPassword));
                user.setEmail(email);
                user.setPhoneNumber(phoneNumber);
                String maskedPhoneNumber = maskPhoneNumber(phoneNumber);
                sendSms(phoneNumber, userRepository.generatorDefaultPassword(6, 6));
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
                        user.setUserName(user.getProfile().getLastName().toLowerCase(Locale.ROOT));
                    }else{
                        username = username + new Random().nextInt(1, userRepository.findAll().size()*2);
                    }

                }
                return ResponseWithGoogle.builder()
                        .id(user.getId())
                        .description(maskedPhoneNumber)
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
                    .id(user.getId())
                    .token(jwtService.createToken(user)).build();

        }
        throw new InvalidConfirmationCode();
    }

    private void sendDefaultPasswordToEmail(User user) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("arstanbeekovvv@gmail.com");
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setText("""
                                              Hi """ + user.getUsername() + """
                                              НИКОМУ НЕ ГОВОРИТЕ КОД!
                                              Это пароль по умолчанию для Peakspace.
                                              Важно изменить этот пароль в целях вашей безопасности.
                                              """
                                      +
                                      user.getPassword()
                                      +
                                      """
                                              Welcome to Peakspace!
                                              """);
            mimeMessageHelper.setSubject("Hello Kyrgyzstan !");
            javaMailSender.send(mimeMessage);
            System.out.println("Mail sent to " + user.getEmail());
        } catch (MessagingException e) {
            throw new NotActiveException();
        }

    }

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
                .userName(user.getUsername())
                .email(user.getEmail())
                .token(jwtService.createToken(user))
                .build();
    }

    private String maskPhoneNumber(String phoneNumber) {
        int length = phoneNumber.length();
        StringBuilder masked = new StringBuilder();
        for (int i = 3; i < length - 2; i++) {
            masked.append("*");
        }
        masked.append(phoneNumber.substring(length - 2));
        return masked.toString();
    }

    public void sendSms(String phoneNumberGetter, String verificationCode) {
        RestTemplate restTemplate = new RestTemplate();

        String requestBody = String.format("{\"phone\": \"%s\", \"message\": \"%s\"}", phoneNumberGetter, verificationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiURL, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
        } else {
            throw new SmsSendingException();
        }
    }

}