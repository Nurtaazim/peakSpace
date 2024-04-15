package peakspace.service.impl;
import com.google.type.PhoneNumber;
import com.twilio.Twilio;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import peakspace.config.jwt.JwtService;
import peakspace.dto.request.RegisterWithGoogleRequest;
import peakspace.dto.response.ResponseWithGoogle;
import peakspace.entities.Profile;
import peakspace.exception.InvalidConfirmationCode;
import peakspace.repository.ProfileRepository;
import peakspace.entities.User;
import peakspace.enums.Role;
import peakspace.exception.FirebaseAuthException;
import peakspace.exception.NotActiveException;
import peakspace.repository.UserRepository;
import peakspace.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UpdatePasswordResponse;

import javax.annotation.processing.Messager;
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
    private static final String accountSID = "SK36c5a0c6fc5df79afa0cc72a9a8d8102";
    private static final String authToken = "0JFf0O8Ynlp1jnjav1ckEIUkIimdpnDI";

    @Override
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
            if (phoneNumber != null && phoneNumber.length() > 2) {
                User user = new User();
                user.setEmail(email);
                user.setPhoneNumber(phoneNumber);
                String maskedPhoneNumber = maskPhoneNumber(phoneNumber);
                user.setPassword(maskedPhoneNumber);
                return ResponseWithGoogle.builder()
                        .id(user.getId())
                        .description(maskedPhoneNumber)
                        .token(tokenFromGoogle)
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
        if(userForVerifier.getPassword().equals(tokenFromGoogle.confirmationCode())) {
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(tokenFromGoogle.tokenGoogle());
                String email = decodedToken.getEmail();
                String fullName = decodedToken.getName();
                String picture = decodedToken.getPicture();
                boolean b = userRepository.existsByEmail(email);
                if (b) throw new NotActiveException();
                String defaultPassword = userRepository.generatorDefaultPassword(8, 8);
                User user = userRepository.getReferenceById(userForVerifier.getId());
                Profile profile = new Profile();
                user.setRole(Role.USER);
                user.setPassword(defaultPassword);
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
                if(userRepository.existsByUserName(user.getProfile().getLastName())){
                    user.setUserName(user.getProfile().getLastName().toLowerCase(Locale.ROOT));
                }
                profile.setAvatar(picture);
                userRepository.save(user);
                profile.setUser(user);
                profileRepository.save(profile);
                user.setProfile(profile);
                sendDefaultPasswordToEmail(user);
                return ResponseWithGoogle.builder()
                        .id(user.getId())
                        .token(jwtService.createToken(user)).build();
            } catch (com.google.firebase.auth.FirebaseAuthException e) {
                throw new FirebaseAuthException();
            }
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

    @Override @Transactional
    public UpdatePasswordResponse updatePassword(PasswordRequest passwordRequest) throws MessagingException {
        if (!passwordRequest.getPassword().equals(passwordRequest.getConfirmPassword())){
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

    public static void sendSms(String phoneNumberGetter, String verificationCode) {
        Twilio.init(accountSID, authToken);

//        Messager message = Message.creator(
//                        new PhoneNumber(phoneNumberGetter), // Номер получателя
//                        new PhoneNumber("Ваш_номер_Twilio"), // Ваш Twilio номер
//                        "Код подтверждения: " + verificationCode) // Текст сообщения
//                .create();

    }


}
