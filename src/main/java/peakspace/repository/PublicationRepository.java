package peakspace.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peakspace.entities.Publication;

@Repository
public interface PublicationRepository extends JpaRepository<Publication,Long> {


    @Modifying
    @Transactional
    @Query("delete from Comment c where c.publication.id =:postId")
    void deleteCom(Long postId);

    @Modifying
    @Transactional
    @Query(value = "delete from publications_link_publications where publication_id =:postId",nativeQuery = true)
    void deleteLink(Long postId);

    @Modifying
    @Transactional
    @Query(value = "delete from publications_tag_friends where publication_id =:postId",nativeQuery = true)
    void deleteTag(Long postId);

    @Modifying
    @Transactional
    @Query(value = "delete from publications_likes where publication_id =:postId",nativeQuery = true)
    void deleteLike(Long postId);
}
