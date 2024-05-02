package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import peakspace.dto.response.SearchResponse;
import peakspace.entities.PablicProfile;
import java.util.List;

@Repository
public interface PublicProfileRepository extends JpaRepository<PablicProfile, Long> {

    @Query("select new peakspace.dto.response.SearchResponse(p.id,p.pablicName,p.avatar) from PablicProfile p where p.pablicName =:keyword")
    List<SearchResponse> findAllPablic(String keyword);

    @Query("select p from PablicProfile p where p.user.id =:foundUserID")
    PablicProfile findByIds(Long foundUserID);

    @Modifying
    @Transactional
    @Query(value = "delete from pablic_profiles_users where pablic_profile_id = :publicId", nativeQuery = true)
    void deleteUsers(Long publicId);
}