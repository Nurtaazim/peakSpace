package peakspace.repository;
import org.springframework.data.jpa.repository.JpaRepository;
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
}