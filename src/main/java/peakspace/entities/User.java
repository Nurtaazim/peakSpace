package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    private Long id;
    private String userName;
    private String email;
    private String password;
    private Boolean isBlock;

    @OneToOne(mappedBy = "user")
    private Profile profile;
    @OneToOne(mappedBy = "user")
    private Like like;
    @OneToMany(mappedBy = "owner")
    private List<Story> stories;
    @OneToMany(mappedBy = "user")
    private List<Chapter> chapters;
    @OneToMany(mappedBy = "user")
    private List<Chat> chats;
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;
    @OneToMany(mappedBy = "user")
    private List<PablicProfile> pablicProfiles;
    @OneToMany(mappedBy = "owner")
    private List<Publication> publications;
    @OneToOne(mappedBy = "userNotification")
    private Notification notification;
//    @OneToMany
//    private List<Notification> notifications;
}
