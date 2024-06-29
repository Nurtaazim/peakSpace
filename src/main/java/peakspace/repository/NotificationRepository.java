package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import peakspace.entities.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "select n from Notification  n where n.like.id = :likeId")
    List<Notification> findByLikeId(long likeId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.publication.id = :publicationId")
    void deleteByPublicationId(@Param("publicationId") Long publicationId);
    @Modifying
    @Query(value = "DELETE FROM notifications WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = :publicationId)", nativeQuery = true)
    void deleteByCommentId(@Param("publicationId") Long publicationId);


    void deleteAllByStoryId(long id);
}