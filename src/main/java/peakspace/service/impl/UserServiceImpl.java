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
import peakspace.exceptions.FirebaseAuthException;
import peakspace.exceptions.NotActiveException;
import peakspace.repository.UserRepository;
import peakspace.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ProfileRepository profileRepository;
    private final JavaMailSender javaMailSender;

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
                                              Hi """+user.getUsername()+"""
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

}
