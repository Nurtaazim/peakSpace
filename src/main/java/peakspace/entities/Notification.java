package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "not_seq")
    @SequenceGenerator(name = "not_seq", allocationSize = 1)
    private Long id;
    private ZonedDateTime createdAt;
    private boolean seen;
    private String notificationMessage;
    @OneToOne
    private Like like;
    @OneToOne
    private User userNotification;
    @OneToOne
    private Comment comment;

}