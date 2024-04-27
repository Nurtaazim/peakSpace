package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peakspace.entities.Chapter;
import peakspace.exception.NotFoundException;

import java.util.List;
import java.util.Map;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    @Query("select ch.id, ch.groupName from Chapter ch where ch.user.id = ?1")
    Map<Long, String> getChaptersById(Long userId);

    default Chapter findByID(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Нет такого раздела с идентификатором: " + id));
    }

    @Query("select c from Chapter c where c.groupName =:section")
    Chapter getByGroupName(String section);

}