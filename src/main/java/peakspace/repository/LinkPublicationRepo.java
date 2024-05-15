package peakspace.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peakspace.entities.Link_Publication;

@Repository
public interface LinkPublicationRepo extends JpaRepository<Link_Publication,Long> {

    @Modifying
    @Transactional
    @Query("delete from Link_Publication l where l.id =:linkId")
    void deleteLink(Long linkId);

}
