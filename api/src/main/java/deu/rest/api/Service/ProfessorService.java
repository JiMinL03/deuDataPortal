package deu.rest.api.Service;

import deu.rest.api.Entity.Professor;
import deu.rest.api.Repository.ProfessorRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final Professor professor = new Professor();

}
