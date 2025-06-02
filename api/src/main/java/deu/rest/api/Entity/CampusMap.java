package deu.rest.api.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//캠퍼스맵
public class CampusMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campusMapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", referencedColumnName = "buildingId")
    private Building buildingId;
    private String content;
    private String etc;
    private String one_layer;
    private String two_layer;
    private String three_layer;
    private String four_layer;
    @Column(length=1000)
    private String five_layer;
    private String six_layer;
    private String seven_layer;
    private String eight_layer;
    private String nine_layer;

}
