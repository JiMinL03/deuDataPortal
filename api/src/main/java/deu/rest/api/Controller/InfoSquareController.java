package deu.rest.api.Controller;

import deu.rest.api.Service.InfoSquareService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Getter
@Setter
@AllArgsConstructor
public class InfoSquareController {

    private final InfoSquareService infoSquareService;

    @GetMapping("/infosquare/fetch")
    public ResponseEntity<String> fetchAll() {
        try {
            infoSquareService.fetchAndSaveAllCategories();  // ✅ 전체 카테고리 크롤링
            return ResponseEntity.ok("모든 카테고리의 공지사항 데이터를 성공적으로 저장했습니다.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("카테고리별 정보 수집 실패: " + e.getMessage());
        }
    }
}
