package deu.rest.api.Repository;

import deu.rest.api.Entity.College;
import deu.rest.api.Entity.Department;
import deu.rest.api.Entity.Tution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutionRepository extends JpaRepository<Tution, Integer> {
    boolean existsByCollege(College college);
}
