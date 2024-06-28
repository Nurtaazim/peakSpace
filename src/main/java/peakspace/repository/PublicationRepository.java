package peakspace.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import peakspace.dto.response.SearchHashtagsResponse;
import peakspace.entities.Publication;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

    @Query("select new peakspace.dto.response.SearchHashtagsResponse(p.id, l) from Publication p inner join p.linkPublications l where lower(p.description) like lower(CONCAT('%', :keyword, '%')) ")
    List<SearchHashtagsResponse> findAllHashtags(@Param("keyword") String keyword);

    @Query("select p from Publication p where p.id =:postId")
    Publication findPostById(Long postId);

    @Query("select p from Publication p join p.owner o where o.id = :ownerId and p.id = :postId")
    Publication findByIdAndOwner(Long postId, Long ownerId);

    @Modifying
    @Transactional
    @Query("delete from Publication p where p.id =:id")
    void deleteByIds(Long id);


}