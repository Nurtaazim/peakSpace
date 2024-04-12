package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peakspace.entities.User;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u where u.email =:email")
    Optional<User> findByEmail(String email);



    @Query("SELECT u FROM User u WHERE u.email = :user OR u.userName = :user OR u.phoneNumber = :user")
    Optional<User> findUserByAll(String user);

    boolean existsByEmail(String email);

    default User getByEmail(String email){
        return findByEmail(email).orElseThrow(() ->
                new NoSuchElementException("User with email: "+email+" not found!"));
    }
}
