package deu.rest.api.Controller;

import deu.rest.api.Service.TutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class TutionController {
    private final TutionService tutionService;
    @GetMapping("/tution/fetch")
    public ResponseEntity<String> fetchColleges() {
        try {
            tutionService.fetchAndSaveTutions();  // 크롤링 + DB 저장 수행
            return ResponseEntity.ok("등록금 데이터를 성공적으로 저장했습니다.");  // 200 OK 응답
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)  // 500 에러 응답
                    .body("데이터 수집 실패: " + e.getMessage());
        }
    }
}
