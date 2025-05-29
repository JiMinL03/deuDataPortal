package deu.rest.api.Repository;

import deu.rest.api.Entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Long> {
    boolean existsByBusName(String busName); //단대 이름 중복 방지용
}
