package deu.rest.api.Service;

import deu.rest.api.Entity.InfoSquare;
import deu.rest.api.Repository.InfoSquareRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoSquareService {

    private final InfoSquareRepository infoSquareRepository;
    private final EntityManager entityManager;

    public void fetchAndSaveInfoSquares() throws IOException {
        int maxPages=5;

        for(int page=1; page<=maxPages; page++){
            String url = "https://www.deu.ac.kr/www/deu-notice.do?page=" + page;
            Document doc = Jsoup.connect(url)
                                .timeout(10000)
                                .get();
            Elements rows = doc.select("tbody > tr");

            if (rows.isEmpty()) break;

            boolean hasValidRow = false;

            for (Element row : rows) {
                String number = row.select("td.num").text().trim();
                String title = row.select("td.subject a").text().trim();
                String writer = row.select("td.name").text().trim();
                String date = row.select("td:nth-child(5)").text().trim();
                String link = row.select("td.subject a").attr("href").trim();

                if (number.isEmpty() || title.isEmpty() || writer.isEmpty()) {
                    continue;
                }

                hasValidRow = true;

                String fullLink = "https://www.deu.ac.kr" + link;

                InfoSquare info = new InfoSquare();
                info.setNumber(number);
                info.setTitle(title);
                info.setWriter(writer);
                info.setDate(date);
                info.setContent(fullLink);

                log.info("저장: 번호={}, 제목={}, 작성자={}, 날짜={}, 링크={}",
                        number, title, writer, date, fullLink);

                infoSquareRepository.save(info);
            }

            if (!hasValidRow) break;

            try {
                Thread.sleep(1000); // 서버 보호를 위해 1초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void resetTable() {
        infoSquareRepository.deleteAll();
        entityManager
                .createNativeQuery("ALTER TABLE INFO_SQUARE ALTER COLUMN ID RESTART WITH 1")
                .executeUpdate();
        log.info("InfoSquare 테이블 초기화 완료 (ID 1부터)");
    }
}
