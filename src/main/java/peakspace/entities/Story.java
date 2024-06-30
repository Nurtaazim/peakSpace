package peakspace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "stories")
@AllArgsConstructor
@NoArgsConstructor
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "story_seq")
    @SequenceGenerator(name = "story_seq", allocationSize = 1, initialValue = 19)
    private Long id;
    private ZonedDateTime createdAt;
    private String text;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private User owner;
    @ManyToMany
    private List<User> tagFriends;
    @ManyToMany()
    private List<Like> likes;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH},fetch = FetchType.LAZY)
    private List<Link_Publication> linkPublications;

    @PrePersist
    public void prePersist() {
        this.createdAt = ZonedDateTime.now();
    }

}