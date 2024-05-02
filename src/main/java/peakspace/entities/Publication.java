package peakspace.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @ElementCollection
    private Map<Long, String> complains;
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
    @ManyToMany()
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