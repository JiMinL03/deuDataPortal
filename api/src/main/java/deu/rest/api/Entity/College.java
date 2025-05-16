package deu.rest.api.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//단과대학 엔티티
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collegeId;
    private String name;
}
