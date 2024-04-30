package peakspace.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import peakspace.dto.response.SearchHashtagsResponse;
import peakspace.entities.Publication;
import peakspace.entities.User;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    @Query("select new peakspace.dto.response.SearchHashtagsResponse(p.id, l) from Publication p inner join p.linkPublications l where lower(p.description) like lower(CONCAT('%', :keyword, '%')) ")
    List<SearchHashtagsResponse> findAllHashtags(@Param("keyword") String keyword);


    @Modifying
    @Transactional
    @Query("delete from Comment c where c.publication.id =:postId")
    void deleteCom(Long postId);

    @Modifying
    @Transactional
    @Query(value = "delete from publications_link_publications where publication_id =:postId", nativeQuery = true)
    void deleteLink(Long postId);

    @Modifying
    @Transactional
    @Query(value = "delete from publications_tag_friends where publication_id =:postId", nativeQuery = true)
    void deleteTag(Long postId);

    @Modifying
    @Transactional
    @Query(value = "delete from publications_likes where publication_id =:postId", nativeQuery = true)
    void deleteLike(Long postId);

    @Query("select p from Publication p where p.id =:postId")
    Publication findPostById(Long postId);

    @Query("select p from Publication p join p.owner o where o = :owner and p.id = :postId")
    Publication findByIdAndOwner(Long postId, User owner);

    @Modifying
    @Transactional
    @Query("delete from Notification n where n.comment.id in (select c.id from Comment c where c.publication.id = :postId)")
    void deleteComNotifications(Long postId);

    @Modifying
    @Transactional
    @Query("delete from Publication p where p.id =:id")
    void deleteByIds(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from publications_link_publications where publication_id = :postId and link_publications_id = :linkId", nativeQuery = true)
    void deletePublicationLink(Long postId, Long linkId);
}