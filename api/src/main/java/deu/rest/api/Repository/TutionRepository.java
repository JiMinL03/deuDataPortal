package deu.rest.api.Repository;

import deu.rest.api.Entity.Tution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutionRepository extends JpaRepository<Tution, Integer> {
}
