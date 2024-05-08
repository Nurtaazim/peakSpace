package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "notifications")
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "not_seq")
    @SequenceGenerator(name = "not_seq", allocationSize = 1,initialValue = 11)
    private Long id;
    private ZonedDateTime createdAt;
    private boolean seen;
    private String notificationMessage;
    @OneToOne
    private Like like;
    @ManyToOne(cascade = {CascadeType.DETACH})
    private User userNotification;   // получатель
    private Long senderUserId;       // отправитель
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private Comment comment;

    public Notification( Like like, User userNotification, Long senderUserId) {
        this.like = like;
        this.userNotification = userNotification;
        this.senderUserId = senderUserId;
    }
    @PrePersist
    void prePersist (){
        this.createdAt = ZonedDateTime.now();
    }
}