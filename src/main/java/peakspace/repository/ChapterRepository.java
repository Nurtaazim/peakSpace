package peakspace.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peakspace.entities.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
}