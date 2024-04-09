package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
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
    @PrePersist
    public void prePersist(){
        this.createdAt = ZonedDateTime.now();
    }
    @PreUpdate
    public void preUpdate(){
        this.createdAt = ZonedDateTime.now();
    }

}