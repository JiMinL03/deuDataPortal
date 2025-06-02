package deu.rest.api.Controller;

import deu.rest.api.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataAggregatorController {
    private final BusService busService;
    private final CampusMapService campusMapService;
    private final CollegeService collegeService;
    private final DepartmentService departmentService;
    private final DeuInfoService deuInfoService;
    private final InfoSquareService infoSquareService;
    private final ProfessorService professorService;
    private final ScheduleService scheduleService;
    private final TutionService tutionService;
    private final UserLinkService userLinkService;

    @GetMapping("/fetch-all")
    public ResponseEntity<String> fetchAllData() {
        StringBuilder result = new StringBuilder();

        try {
            userLinkService.fetchAndSaveUserLink();
            result.append("✅ 사용자 링크 저장 완료\n");

            deuInfoService.fetchAndSaveDeuInfo();
            result.append("✅ 학교 정보 저장 완료\n");

            collegeService.fetchAndSaveColleges();
            result.append("✅ 단과대학 저장 완료\n");

            tutionService.fetchAndSaveTutions();
            result.append("✅ 등록금 저장 완료\n");

            departmentService.fetchAndSaveDepartment();
            result.append("✅ 학과 저장 완료\n");

            professorService.crawlAllProfessors();
            result.append("✅ 교수 저장 완료\n");

            scheduleService.saveSchedulesByYearMonth();
            result.append("✅ 시간표 저장 완료\n");

            campusMapService.fetchAndSaveCampusMap();
            result.append("✅ 캠퍼스 맵 저장 완료\n");

            infoSquareService.fetchAndSaveAllCategories();
            result.append("✅ 정보광장 저장 완료\n");

            busService.fetchAndSaveBus();
            result.append("✅ 버스정보 저장 완료\n");

        } catch (IOException e) {
            result.append("❌ 오류 발생: ").append(e.getMessage()).append("\n");
            return ResponseEntity.internalServerError().body(result.toString());
        }

        return ResponseEntity.ok(result.toString());
    }
}