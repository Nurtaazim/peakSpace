package peakspace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "link_publications")
@AllArgsConstructor
@NoArgsConstructor
public class Link_Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "link_seq")
    @SequenceGenerator(name = "link_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    @Column(length = 10000)
    private String link;

}