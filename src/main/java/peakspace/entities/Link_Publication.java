package peakspace.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Entity
@Table(name = "link_publications")
public class Link_Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "link_seq")
    @SequenceGenerator(name = "link_seq", allocationSize = 1, initialValue = 21)
    private Long id;
    @Column(length = 10000)
    private String link;

}