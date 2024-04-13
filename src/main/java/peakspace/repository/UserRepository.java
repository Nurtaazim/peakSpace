package peakspace.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import peakspace.dto.response.SearchResponse;
import peakspace.entities.User;
import peakspace.exception.NotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email =:email")
    Optional<User> findByEmail(String email);

    default User getByEmail(String email){
        return findByEmail(email).orElseThrow(() ->
                new NotFoundException("User with email: "+email+" not found!"));
    }

    @Query("select new peakspace.dto.response.SearchResponse(u.id, u.userName, p.avatar, p.aboutYourSelf) " +
            "from User u left join u.profile p where lower(u.userName) like lower(concat('%', :keyword, '%'))")
    List<SearchResponse>    findAllSearch(@Param("keyword") String keyword);
}