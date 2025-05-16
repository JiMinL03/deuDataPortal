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
//정보광장엔티티
public class InfoSquare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long infoSquareId;

    private String writer;
}
