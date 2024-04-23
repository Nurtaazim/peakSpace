package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peakspace.entities.Education;

@Repository
public interface EducationRepository extends JpaRepository <Education,Long> {
}
