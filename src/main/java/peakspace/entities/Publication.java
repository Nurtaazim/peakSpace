package peakspace.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "publications")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "publications_seq")
    @SequenceGenerator(name = "publications_seq", allocationSize = 1,initialValue = 21)
    private Long id;
    private String description;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String location;
    private boolean isBlockComment;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private User owner;
    @ManyToMany
    private List<User> tagFriends;
    @ManyToMany
    private List<Link_Publication> linkPublications = new ArrayList<>();
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