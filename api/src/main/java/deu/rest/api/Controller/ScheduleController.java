package deu.rest.api.Controller;

import deu.rest.api.Entity.Schedule;
import deu.rest.api.Repository.ScheduleRepository;
import deu.rest.api.Service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;

    // 1) 학사일정 크롤링 및 저장
    @GetMapping("/schdule/fetch")
    public ResponseEntity<String> fetchSchedule() {
        try {
            scheduleService.saveSchedulesByYearMonth();
            return ResponseEntity.ok("학사일정 저장 성공");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("실패: " + e.getMessage());
        }
    }

}
