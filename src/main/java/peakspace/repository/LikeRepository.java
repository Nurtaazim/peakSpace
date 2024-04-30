package peakspace.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peakspace.entities.Like;
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    @Modifying
    @Transactional
    @Query(value = "delete FROM Like u\n" +
            "WHERE u.id = :id")
    void delete(long id);
}
