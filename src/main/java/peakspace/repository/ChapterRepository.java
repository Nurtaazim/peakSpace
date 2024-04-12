package peakspace.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peakspace.entities.Chapter;
import peakspace.exception.NotFoundException;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    default Chapter findByID(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Нет такого раздела с идентификатором: " + id));
    }
}