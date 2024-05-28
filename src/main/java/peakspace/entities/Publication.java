package peakspace.entities;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publications_seq")
    @SequenceGenerator(name = "publications_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    private String description;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String location;
    private boolean isBlockComment;
    @ElementCollection
    private Map<Long, String> complains;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH},fetch = FetchType.EAGER)
    private User owner;
    @ManyToMany(cascade = {CascadeType.PERSIST},fetch = FetchType.EAGER)
    private List<User> tagFriends;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
    private List<Link_Publication> linkPublications = new ArrayList<>();
    @OneToMany(mappedBy = "publication", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<Comment> comments;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private PablicProfile pablicProfile;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Like> likes;
    @PrePersist
    public void prePersist() {
        this.createdAt = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.createdAt = ZonedDateTime.now();
    }

}