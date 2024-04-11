package peakspace.service.impl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peakspace.config.jwt.JwtService;
import peakspace.dto.response.ResponseWithGoogle;
import peakspace.entities.User;
import peakspace.exceptions.FirebaseAuthException;
import peakspace.exceptions.NotActiveException;
import peakspace.repository.UserRepository;
import peakspace.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

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
        }catch (com.google.firebase.auth.FirebaseAuthException e){
            throw new FirebaseAuthException();
        }
    }

}
