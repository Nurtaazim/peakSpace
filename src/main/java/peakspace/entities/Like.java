package peakspace.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "likes_seq", allocationSize = 1)
    private Long id;
    private boolean isLike;

    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.PERSIST})
    private User user;

}