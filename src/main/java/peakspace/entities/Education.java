package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import peakspace.enums.Studies;

@Getter
@Setter
@Entity
@Table(name = "educations")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "edu_seq", allocationSize = 1)
    private Long id;
    private Studies avgAndHigher;
    private String city;
    private String educationalInstitution;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.PERSIST})
    private Profile profile;

}