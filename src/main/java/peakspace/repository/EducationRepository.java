package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import peakspace.entities.Education;

public interface EducationRepository extends JpaRepository <Education,Long> {

    @Modifying
    @Transactional
    @Query("delete from Education e where e.id=:eduId")
    void deleteEducation(Long eduId);
}
