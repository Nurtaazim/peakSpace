package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peakspace.entities.Profile;
import peakspace.entities.User;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {


}
