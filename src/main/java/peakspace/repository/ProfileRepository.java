package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peakspace.entities.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {
}

