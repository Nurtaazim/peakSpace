package peakspace.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    private Long id;
    private String userName;
    private String email;
    private String password;
    private Boolean isBlock;
    @OneToOne(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Profile profile;
    @OneToMany(mappedBy = "owner",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Story> stories;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Chapter> chapters;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<Chat> chats;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<Comment> comments;
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<PablicProfile> pablicProfiles;
    @OneToMany(mappedBy = "owner",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Publication> publications;
    @OneToOne(mappedBy = "userNotification",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Notification notification;

}