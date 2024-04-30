package peakspace.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peakspace.entities.Notification;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Modifying
    @Transactional
    @Query(value = "delete FROM Notification u\n" +
            "WHERE u.id = :id")
    void deleteByid(long id);
}
