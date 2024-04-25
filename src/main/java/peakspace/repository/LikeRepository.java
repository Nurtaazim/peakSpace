package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peakspace.entities.Like;
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
}
