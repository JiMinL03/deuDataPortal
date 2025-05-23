package deu.rest.api.Repository;

import deu.rest.api.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    boolean existsByDepartmentName(String departmentName);
    Optional<Department> findByDepartmentName(String departmentName);
}
