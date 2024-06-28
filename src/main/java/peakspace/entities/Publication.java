package peakspace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "publications")
@AllArgsConstructor
@NoArgsConstructor
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
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH},fetch = FetchType.LAZY)
    private User owner;
    @ManyToMany(cascade = {CascadeType.PERSIST},fetch = FetchType.LAZY)
    private List<User> tagFriends;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
    private List<Link_Publication> linkPublications = new ArrayList<>();
    @OneToMany(mappedBy = "publication", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<Comment> comments;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private PablicProfile pablicProfile;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Like> likes;
    @PrePersist
    public void prePersist() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Bishkek"));
    }

    @PreUpdate
    public void preUpdate() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Bishkek"));
    }

}