package peakspace.service;

import com.google.firebase.auth.FirebaseAuthException;
import org.hibernate.type.descriptor.java.JdbcTimeJavaType;
import peakspace.dto.response.ResponseWithGoogle;

import java.io.NotActiveException;

public interface UserService {
    ResponseWithGoogle verifyToken(String tokenFromGoogle);
}
