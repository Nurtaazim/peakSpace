package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peakspace.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}