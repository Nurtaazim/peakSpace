package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import peakspace.dto.response.*;
import peakspace.entities.Profile;
import peakspace.entities.User;
import peakspace.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User getReferenceByEmail(String email);


    @Query("select u from User u where u.email =:email")
    Optional<User> findByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.userName = :userName")
    boolean existsByThisUserName(@Param("userName") String userName);

    default User getByEmail(String email) {
        return findByEmail(email).orElseThrow(() ->
                new NotFoundException("Пользователь с email '" + email + "' не найден в базе!"));
    }

    @Query("select u from  User u where u.userName like :email")
    Optional<User> getByUserName(String email);

    @Query("select new peakspace.dto.response.SearchResponse(u.id, u.userName, p.avatar, p.aboutYourSelf) " +
            "from User u left join u.profile p where lower(u.userName) like lower(concat('%', :keyword, '%'))")
    List<SearchResponse> findAllSearch(@Param("keyword") String keyword);

    default User findByIds(Long foundUserId) {
        return findById(foundUserId).orElseThrow(() -> new NotFoundException(" Нет такой ползователь !" + foundUserId));
    }

    @Query("select new peakspace.dto.response.ProfileFriendsResponse(u.id, COALESCE(p.avatar, ''), COALESCE(p.cover, ''),COALESCE(u.userName, ''), COALESCE(p.aboutYourSelf, ''), COALESCE(p.profession, '')) " +
            "from User u left join u.profile p " +
            "where u.id = :foundUserId")
    ProfileFriendsResponse getId(Long foundUserId);

    @Query("select p from Profile p where  p.id=:id")
    Profile findBYProfile(Long id);

    boolean existsByUserName(String userName);

    @Query("select new peakspace.dto.response.ChapTerResponse(c.id,c.groupName) from Chapter c join c.user u where lower(c.groupName) like lower(concat('%' ,:search ,'%') )")
    List<ChapTerResponse> searchChapter(String search);

    @Query("select distinct new peakspace.dto.response.SearchUserResponse(u.id,u.userName,u.profile.firstName,u.profile.lastName,u.profile.cover,u.profile.avatar,u.profile.profession) from User u left join u.publications ups where u.userName ilike :keyWord OR u.profile.lastName ilike :keyWord OR u.profile.firstName ilike :keyWord OR u.profile.patronymicName ilike :keyWord OR u.profile.profession ilike :keyWord")
    List<SearchUserResponse> findByAll(String keyWord);

    default User getCurrentUser() {
        return findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new NotFoundException("Пользователь с такой емайл не найдено!"));
    }

    @Query("select u from User u where u.confirmationCode=:uuid")
    Optional<User> findByUuid(String uuid);

    @Query("select u from User u where u.userName = :userName")
    Optional<User> findByName(String userName);

    @Query(value = """
            select new peakspace.dto.response.UserResponse(
                u.id,
                u.profile.avatar,
                u.profile.firstName,
                u.profile.lastName,
                u.userName,
                u.email
            )
            from User u
            where u.id not in :userIds
            """)
    List<UserResponse> findAllNotInWithIds(List<Long> userIds);

}
