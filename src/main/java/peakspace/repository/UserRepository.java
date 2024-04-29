package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import peakspace.dto.response.ChapTerResponse;
import peakspace.dto.response.UserMarkResponse;
import peakspace.entities.Publication;
import peakspace.dto.response.ProfileFriendsResponse;
import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.SearchResponse;
import peakspace.entities.Profile;
import peakspace.entities.User;
import peakspace.exception.NotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User getReferenceByEmail(String email);


    @Query("select u from User u where u.email =:email")
    Optional<User> findByEmail(String email);

    default User getByEmail(String email) {
        return findByEmail(email).orElseThrow(() ->
                new NotFoundException("Пользователь с email '" + email + "' не найден в базе!"));
    }
    @Query("select u from  User u where u.userName like :email")
    Optional<User> getByUserName(String email);


    @Query("select new peakspace.dto.response.SearchResponse(u.id, u.userName, p.avatar, p.aboutYourSelf) " +
           "from User u left join u.profile p where lower(u.userName) like lower(concat('%', :keyword, '%'))")
    List<SearchResponse> findAllSearch(@Param("keyword") String keyword);


    @Query("select new peakspace.dto.response.SearchResponse(u.id, u.userName, p.avatar, p.aboutYourSelf) " +
            "from User u left join u.profile p")
    List<SearchResponse> findAllSearchEmpty();

    default User findByIds(Long foundUserId) {
        return findById(foundUserId).orElseThrow(() -> new NotFoundException(" Нет такой ползователь !" + foundUserId));
    }

    @Query("select new peakspace.dto.response.ProfileFriendsResponse(u.id, COALESCE(p.avatar, ''), COALESCE(p.cover, ''), COALESCE(p.aboutYourSelf, ''), COALESCE(p.profession, '')) " +
           "from User u left join u.profile p " +
           "where u.id = :foundUserId")
    ProfileFriendsResponse getId(Long foundUserId);

    @Query("select new peakspace.dto.response.PublicationResponse(p.id) from Publication p join p.owner.profile pr where pr.id = :foundUserId and p.id in (select f from Profile pf join pf.favorites f where pf.id = :foundUserId)")
    List<PublicationResponse> findFavorite(Long foundUserId);

    @Query("select new peakspace.dto.response.PublicationResponse(p.id) from Publication p join p.tagFriends tg where tg.id = :foundUserId and p.owner.id = :foundUserId")
    List<PublicationResponse> findTagWithMe(Long foundUserId);

    @Query("select p from Profile p where  p.id=:id")
    Profile findBYProfile(Long id);

    boolean existsByUserName(String userName);

    @Query("select new peakspace.dto.response.ChapTerResponse(c.id,c.groupName) from Chapter c join c.user u where lower(c.groupName) like lower(concat('%' ,:search ,'%') )")
    List<ChapTerResponse> searchChapter(String search);

    @Query("select p from Publication p where p.owner.id =:friendId")
    List<Publication> findFriendsPub(Long friendId);

    @Query("select new peakspace.dto.response.UserMarkResponse(u.id, u.userName) from User u where u.id in :foundUserId")
    List<UserMarkResponse> findFoundUserId(List<Long> foundUserId);
}