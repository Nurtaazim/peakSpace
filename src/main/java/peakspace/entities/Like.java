package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;

@Getter
@Setter
@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "likes_seq", allocationSize = 1,initialValue = 9)
    private Long id;
    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.PERSIST})
    private User user;

}