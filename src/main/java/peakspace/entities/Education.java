package peakspace.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import peakspace.enums.Studies;

@Getter
@Setter
@Entity
@Table(name = "educations")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "edu_seq", allocationSize = 1,initialValue = 9)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Studies avgAndHigher;
    private String city;
    private String educationalInstitution;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.PERSIST})
    private Profile profile;

}