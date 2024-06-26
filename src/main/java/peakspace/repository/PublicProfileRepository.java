package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peakspace.dto.response.SearchResponse;
import peakspace.entities.PablicProfile;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicProfileRepository extends JpaRepository<PablicProfile, Long> {

    @Query("select new peakspace.dto.response.SearchResponse(p.id,p.pablicName,p.avatar) from PablicProfile p where p.pablicName =:keyword")
    List<SearchResponse> findAllPablic(String keyword);

    @Query("select p from PablicProfile p where p.pablicName=:publicName")
    Optional<PablicProfile> findByPublicName(String publicName);

}