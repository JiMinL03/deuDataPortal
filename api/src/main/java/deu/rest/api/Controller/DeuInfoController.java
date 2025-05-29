package deu.rest.api.Controller;

import deu.rest.api.Service.DeuInfoService;
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
public class DeuInfoController {
    private final DeuInfoService deuInfoService;
    @GetMapping("/deuinfo/fetch")
    public ResponseEntity<String> fetchDeuInfo() {
        try{
            deuInfoService.fetchAndSaveDeuInfo();
            return ResponseEntity.ok("동의대 정보 성공적 저장");
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)  // 500 에러 응답
                    .body("데이터 수집 실패: " + e.getMessage());
        }
    }
}
