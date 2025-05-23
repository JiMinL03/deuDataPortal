package deu.rest.api.Service;

import deu.rest.api.Entity.Tution;
import deu.rest.api.Repository.TutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TutionService {
    private final TutionRepository tutionRepository;
    private final Tution tution=new Tution();

}
