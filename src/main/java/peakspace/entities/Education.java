package peakspace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peakspace.enums.Country;

@Getter
@Setter
@Entity
@Table(name = "educations")
@AllArgsConstructor
@NoArgsConstructor
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "edu_seq")
    @SequenceGenerator(name = "edu_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Country country;
    private String educationalInstitution;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Profile profile;

}