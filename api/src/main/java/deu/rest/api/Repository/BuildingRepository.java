package deu.rest.api.Repository;

import deu.rest.api.Entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    Optional<Building> findByBuildingId(int buildingId);
}
