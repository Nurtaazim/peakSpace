package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    private User owner;
    @OneToMany
    private List<User> tagFriends;
    @OneToMany(mappedBy = "publication")
    private List<Link_Publication> linkPublications;
    @OneToMany(mappedBy = "publication")
    private List<Comment> comments;
    @ManyToOne
    private PablicProfile pablicProfile;
    @OneToMany()
    private List<Like> likes;
}
