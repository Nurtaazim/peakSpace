package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peakspace.entities.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}