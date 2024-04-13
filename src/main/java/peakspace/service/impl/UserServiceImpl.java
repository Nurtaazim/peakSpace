package peakspace.service.impl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import peakspace.config.jwt.JwtService;
import peakspace.dto.response.ResponseWithGoogle;
import peakspace.entities.Profile;
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

    @Override
    public ResponseWithGoogle verifyToken(String tokenFromGoogle) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(tokenFromGoogle);
            String email = decodedToken.getEmail();
            boolean b = userRepository.existsByEmail(email);
            if (b) {
                User user = userRepository.getReferenceByEmail(email);
                return ResponseWithGoogle.builder()
                        .id(user.getId())
                        .token(jwtService.createToken(user))
                        .build();
            }
            throw new NotActiveException();
        } catch (com.google.firebase.auth.FirebaseAuthException e) {
            throw new FirebaseAuthException();
        }
    }

    @Override
    public ResponseWithGoogle signUpWithGoogle(String tokenFromGoogle) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(tokenFromGoogle);
            String email = decodedToken.getEmail();
            String fullName = decodedToken.getName();
            String picture = decodedToken.getPicture();
            boolean b = userRepository.existsByEmail(email);
            if (b) throw new NotActiveException();
            String defaultPassword = userRepository.generatorDefaultPassword(8, 8);
            String defaultUserName = userRepository.generatorDefaultPassword(6, 13);
            User user = new User();
            Profile profile = new Profile();
            user.setEmail(email);
            user.setRole(Role.USER);
            user.setPassword(defaultPassword);
            user.setUserName(defaultUserName);
            profile.setFullName(fullName);
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

}
