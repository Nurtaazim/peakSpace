package peakspace.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "publications")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "publications_seq", allocationSize = 1)
    private Long id;
    private String description;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String location;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private User owner;
    @OneToMany
    private List<User> tagFriends;
    @OneToMany
    private List<Link_Publication> linkPublications;
    @OneToMany(mappedBy = "publication",cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<Comment> comments;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private PablicProfile pablicProfile;
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Like> likes;
}
