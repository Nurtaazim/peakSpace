package peakspace.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    @Entity
    @Table(name = "link_publications")
    public class Link_Publication {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "link_seq")
        @SequenceGenerator(name = "link_seq", allocationSize = 1,initialValue = 21)
        private Long id;
        private String link;

    }