package deu.rest.api.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tution {
    @Id
    private Integer collegeId;

    @OneToOne
    @MapsId  // collegeId를 외래키와 기본키로 같이 사용
    @JoinColumn(name = "college_id")
    private College college;

    private String tuition;
}
