package deu.rest.api.Service;

import deu.rest.api.Repository.DepartmentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import deu.rest.api.Entity.Department;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public Long join(Department department) {
        validateDuplicateMember(department); //중복 회원 검증
        departmentRepository.save(department);
        return department.getDepartmentId();
    }
    private void validateDuplicateMember(Department department) {
        departmentRepository.findById(department.getDepartmentId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public List<Department> findMembers() {
        return departmentRepository.findAll();
    }
    public Optional<Department> findOne(Long memberId) {
        return departmentRepository.findById(memberId);
    }
}
