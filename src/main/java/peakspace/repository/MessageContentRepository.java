package peakspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import peakspace.entities.MessageContent;

public interface MessageContentRepository extends JpaRepository<MessageContent, Long> {

}