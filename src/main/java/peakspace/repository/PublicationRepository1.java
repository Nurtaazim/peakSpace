package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peakspace.entities.Publication;

public interface PublicationRepository1 extends JpaRepository<Publication, Long> {
}