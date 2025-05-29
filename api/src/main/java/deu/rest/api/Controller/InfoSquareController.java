package deu.rest.api.Controller;

import deu.rest.api.Service.InfoSquareService;
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
public class InfoSquareController {

    private final InfoSquareService infoSquareService;

    @GetMapping("/infosquare/fetch")  // 공지사항 크롤링 및 저장 실행
    public ResponseEntity<String> fetchInfoSquares() {
        try {
            infoSquareService.fetchAndSaveInfoSquares();  // Jsoup 크롤링 + DB 저장
            return ResponseEntity.ok("정보광장 데이터를 성공적으로 저장했습니다.");
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("정보광장 데이터 수집 실패: " + e.getMessage());
        }
    }
}
