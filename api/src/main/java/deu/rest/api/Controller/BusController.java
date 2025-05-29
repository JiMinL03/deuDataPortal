package deu.rest.api.Controller;

import deu.rest.api.Service.BusService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@Getter
@Setter
@AllArgsConstructor
public class BusController {
    private final BusService busService;
    @GetMapping("/bus/fetch")  // HTTP POST 요청을 "/fetch" 경로로 받을 때 실행됨
    public ResponseEntity<String> fetchColleges() {
        try {
            busService.fetchAndSaveBus();  // 크롤링 + DB 저장 수행
            return ResponseEntity.ok("버스노선 데이터를 성공적으로 저장했습니다.");  // 200 OK 응답
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)  // 500 에러 응답
                    .body("데이터 수집 실패: " + e.getMessage());
        }
    }
}
