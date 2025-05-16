package deu.rest.api.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//셔틀버스 엔티티
public class Bus {
    @Id
    private int busName; //버스번호

    private String route; //노선
    private String firstBus; //첫차
    private String lastBus; //막차
    private String dispatchTime; //배차시간

}
