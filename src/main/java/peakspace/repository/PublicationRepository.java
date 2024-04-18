package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peakspace.entities.Publication;

@Repository
public interface PublicationRepository extends JpaRepository<Publication,Long> {
}
