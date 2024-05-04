package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peakspace.entities.Profile;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {

    @Query("select p from Profile p where p.phoneNumber = :phoneNumber")
    Profile findByPhoneNumber(String phoneNumber);
}

