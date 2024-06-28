package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peakspace.entities.Chapter;
import peakspace.exception.NotFoundException;


public interface ChapterRepository extends JpaRepository<Chapter, Long> {

}