package peakspace.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import peakspace.dto.response.SearchHashtagsResponse;
import peakspace.entities.Publication;

import java.util.List;


@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    @Query("SELECT new peakspace.dto.response.SearchHashtagsResponse(p.id, l) FROM Publication p INNER JOIN p.linkPublications l WHERE LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
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

}