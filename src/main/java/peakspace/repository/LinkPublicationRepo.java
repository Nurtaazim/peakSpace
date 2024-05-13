package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peakspace.entities.Link_Publication;
@Repository
public interface LinkPublicationRepo extends JpaRepository<Link_Publication, Long> {
}
