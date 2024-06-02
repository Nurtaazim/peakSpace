package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peakspace.entities.Notification;
import peakspace.entities.User;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "select n from Notification  n where n.like.id = :likeId")
    Optional<Notification> findByLikeId(long likeId);

}