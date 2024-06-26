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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "likes_seq")
    @SequenceGenerator(name = "likes_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private User user;


}