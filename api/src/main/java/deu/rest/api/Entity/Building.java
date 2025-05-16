package deu.rest.api.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//캠퍼스맵 건물
public class Building {
    @Id
    private int buildingId; //건물번호
    private String buildingName; //건물이름
}
