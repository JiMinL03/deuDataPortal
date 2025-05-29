package deu.rest.api.Repository;

import deu.rest.api.Entity.Building;
import deu.rest.api.Entity.Bus;
import deu.rest.api.Entity.CampusMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusMapRepository extends JpaRepository<CampusMap, Long> {
    boolean existsByBuildingId(Building buildingId); //단대 이름 중복 방지용
}
