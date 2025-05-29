package deu.rest.api.Repository;

import deu.rest.api.Entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByMonthvalue(String monthvalue);
}
