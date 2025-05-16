package deu.rest.api.Controller;

import deu.rest.api.Entity.Department;
import deu.rest.api.Service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/Department/{id}")
    public Optional<Department> getDepartment(@PathVariable("id") Long id) {
        return departmentService.findOne(id);
    }

    @GetMapping("/Department/all")
    public List<Department> getAllMember() {
        return departmentService.findMembers();
    }

    @PutMapping("/Department/new/{name}")
    public void putMember(@PathVariable("name") String name) {
        Department department = new Department();
        department.setDepartmentName(name);

        departmentService.join(department);
    }
}
