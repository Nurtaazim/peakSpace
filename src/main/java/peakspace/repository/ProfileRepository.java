package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import peakspace.entities.Profile;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    @Query("select p from Profile p where p.phoneNumber = :phoneNumber")
    Profile findByPhoneNumber(String phoneNumber);
}

