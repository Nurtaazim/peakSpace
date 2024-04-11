package peakspace.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import peakspace.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User getReferenceByEmail(String email);
}
