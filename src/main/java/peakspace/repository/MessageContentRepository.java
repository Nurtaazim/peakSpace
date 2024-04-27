package peakspace.repository;

import com.fasterxml.jackson.annotation.OptBoolean;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import peakspace.dto.response.MessageResponse;
import peakspace.entities.MessageContent;

import java.util.List;
import java.util.Optional;

public interface MessageContentRepository extends JpaRepository<MessageContent, Long> {

}