package peakspace.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import peakspace.entities.User;
import java.util.Random;
import org.springframework.data.jpa.repository.Query;
import peakspace.exception.NotFoundException;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User getReferenceByEmail(String email);

    default String generatorDefaultPassword(int minLength, int maxLength) {
        Random random = new Random();
        int length = + random.nextInt(maxLength - minLength + 1);
        StringBuilder sb = new StringBuilder(length);
        String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    @Query("select u from User u where u.email =:email")
    Optional<User> findByEmail(String email);

    default User getByEmail(String email){
        return findByEmail(email).orElseThrow(() ->
                new NotFoundException("User with email: "+email+" not found!"));
    }
    @Query("select u from  User u where u.userName like :email")
    Optional<User> getByUserName(String email);


    boolean existsByUserName(String userName);
}