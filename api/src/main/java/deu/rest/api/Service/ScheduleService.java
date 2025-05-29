package deu.rest.api.Service;

import deu.rest.api.Entity.Schedule;
import deu.rest.api.Repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public void saveSchedulesByYearMonth() throws IOException {
        String url = "https://www.deu.ac.kr/www/scheduleList.do";
        Document doc = Jsoup.connect(url).get();

        // 한 달 단위 div.inner 선택
        Elements months = doc.select("div.inner");

        for (Element monthElem : months) {
            Element monthTitleElem = monthElem.selectFirst("h5.h5-tit");
            if (monthTitleElem == null) {
                // 월 제목 없으면 다음으로
                continue;
            }
            String month = monthTitleElem.text().trim();

            // 상세 일정 테이블 행들
            Elements rows = monthElem.select("table.tbl-basic-dleft tbody tr");
            for (Element row : rows) {
                Element dayElem = row.selectFirst("th");
                Element contentElem = row.selectFirst("td");

                if (dayElem == null || contentElem == null) {
                    // 날짜나 내용 없으면 다음으로
                    continue;
                }

                String day = dayElem.text().trim();
                String content = contentElem.text().trim();

                String monthDay = month + " " + day;

                // 기존 DB에 있으면 업데이트, 없으면 새로 저장
                Schedule schedule = scheduleRepository.findByMonthvalue(monthDay)
                        .orElse(new Schedule());

                schedule.setMonthvalue(monthDay);
                schedule.setContents(content);

                scheduleRepository.save(schedule);
            }
        }
    }
}
