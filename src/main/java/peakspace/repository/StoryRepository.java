package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peakspace.entities.Story;
@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {



}
