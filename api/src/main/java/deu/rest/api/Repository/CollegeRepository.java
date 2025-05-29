package deu.rest.api.Repository;

import deu.rest.api.Entity.College;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollegeRepository extends JpaRepository<College, Integer> {
    boolean existsByName(String name); //단대 이름 중복 방지용
    Optional<College> findByName(String collegeName);
}
