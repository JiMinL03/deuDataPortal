package deu.rest.api.Repository;

import deu.rest.api.Entity.DeuInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeuInfoRepository extends JpaRepository<DeuInfo, Long> {
    boolean existsByDeuName(String deuName);
}