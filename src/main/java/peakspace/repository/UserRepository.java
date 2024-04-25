package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import peakspace.dto.response.ProfileFriendsResponse;
import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.SearchResponse;

import peakspace.dto.response.SearchUserResponse;
import peakspace.entities.Profile;
import peakspace.entities.User;
import peakspace.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User getReferenceByEmail(String email);

    default String generatorDefaultPassword(int minLength, int maxLength) {
        Random random = new Random();
        int length = +random.nextInt(maxLength - minLength + 1);
        StringBuilder sb = new StringBuilder(length);
        String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    @Query("select u from User u where u.email =:email")
    Optional<User> findByEmail(String email);

    default User getByEmail(String email) {
        return findByEmail(email).orElseThrow(() ->
                new NotFoundException("Нет такой : " + email + " в базе !"));
    }

    @Query("select new peakspace.dto.response.SearchResponse(u.id, u.userName, p.avatar, p.aboutYourSelf) " +
           "from User u left join u.profile p where lower(u.userName) like lower(concat('%', :keyword, '%'))")
    List<SearchResponse> findAllSearch(@Param("keyword") String keyword);


    @Query("select new peakspace.dto.response.SearchResponse(u.id, u.userName, p.avatar, p.aboutYourSelf) " +
           "from User u left join u.profile p ")
    List<SearchResponse> findAllSearchEmpty();

    default User findByIds(Long foundUserId) {
        return findById(foundUserId).orElseThrow(() -> new NotFoundException(" Нет такой ползователь !" + foundUserId));
    }

    @Query("SELECT new peakspace.dto.response.ProfileFriendsResponse(u.id, COALESCE(p.avatar, ''), COALESCE(p.cover, ''), COALESCE(p.aboutYourSelf, ''), COALESCE(p.profession, '')) " +
           "FROM User u LEFT JOIN u.profile p " +
           "WHERE u.id = :foundUserId")
    ProfileFriendsResponse getId(Long foundUserId);

    @Query("select new peakspace.dto.response.PublicationResponse(p.id) from Publication p where p.owner.id =:foundUserId")
    List<PublicationResponse> findFriendsPublic(Long foundUserId);

    @Query("select new peakspace.dto.response.PublicationResponse(p.id) from Publication p join p.owner.profile pr where pr.id = :foundUserId and p.id in (select f from Profile pf join pf.favorites f where pf.id = :foundUserId)")
    List<PublicationResponse> findFavorite(Long foundUserId);

    @Query("select new peakspace.dto.response.PublicationResponse(p.id) from Publication p join p.tagFriends tg where tg.id = :foundUserId and p.owner.id = :foundUserId")
    List<PublicationResponse> findTagWithMe(Long foundUserId);

    @Query("select p from Profile p where  p.id=:id")
    Profile findBYProfile(Long id);

    @Query("select distinct new peakspace.dto.response.SearchUserResponse(u.id,u.userName,u.profile.firstName,u.profile.lastName,u.profile.cover,u.profile.avatar,u.profile.profession) from User u left join u.publications ups where u.userName ilike :keyWord OR ups.description ilike :keyWord OR u.profile.lastName ilike :keyWord OR u.profile.firstName ilike :keyWord OR u.profile.profession ilike :keyWord")
    List<SearchUserResponse> findByAll(String keyWord);
}