package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import peakspace.entities.Chapter;
import peakspace.exception.NotFoundException;

import java.util.Map;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    default Chapter findByID(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Нет такого раздела с идентификатором: " + id));
    }

}