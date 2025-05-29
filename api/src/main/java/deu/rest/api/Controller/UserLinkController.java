package deu.rest.api.Controller;

import deu.rest.api.Service.BusService;
import deu.rest.api.Service.UserLinkService;
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
public class UserLinkController {
    private final UserLinkService userLinkService;
    @GetMapping("/userLink/fetch")  // HTTP POST 요청을 "/fetch" 경로로 받을 때 실행됨
    public ResponseEntity<String> fetchColleges() {
        try {
            userLinkService.fetchAndSaveUserLink();  // 크롤링 + DB 저장 수행
            return ResponseEntity.ok("사용자별 서비스 링크를 성공적으로 저장했습니다.");  // 200 OK 응답
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)  // 500 에러 응답
                    .body("데이터 수집 실패: " + e.getMessage());
        }
    }
}
