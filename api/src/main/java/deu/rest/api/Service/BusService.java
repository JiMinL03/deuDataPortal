package deu.rest.api.Service;

import deu.rest.api.Entity.Bus;
import deu.rest.api.Repository.BusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusService {
    private final BusRepository busRepository;
    public void fetchAndSaveColleges() throws IOException { //단과대학 정보를 가져오고 저장하는 메서드
        Document doc = Jsoup.connect("https://www.deu.ac.kr/www/deu-bus.do").get(); // Jsoup 라이브러리를 사용해 웹 페이지를 크롤링
        /*
        * Jsoup.connect(...) 주어진 URL에 HTTP 연결을 시도
        * .get() → 해당 웹페이지의 HTML 문서 전체를 가져와서 파싱한 후, Document 객체로 반환
        * Document doc → 웹페이지의 HTML DOM 전체 구조를 담고 있는 객체
        * */
        Elements rows = doc.select("table tbody tr");

        for (Element row : rows) {
            Elements cols = row.select("td");

            if (cols.size() < 5) continue;  // 컬럼 개수 부족 시 건너뜀

            String busName = cols.get(0).text().trim();
            String route = cols.get(1).text().trim();
            String firstBus = cols.get(2).text().trim();
            String lastBus = cols.get(3).text().trim();
            String dispatchTime = cols.get(4).text().trim();

            log.info("버스번호: {}, 노선: {}, 첫차: {}, 막차: {}, 배차시간: {}", busName, route, firstBus, lastBus, dispatchTime);

            if (!busRepository.existsByBusName(busName)) {
                Bus bus = new Bus();
                bus.setBusName(busName);
                bus.setRoute(route);
                bus.setFirstBus(firstBus);
                bus.setLastBus(lastBus);
                bus.setDispatchTime(dispatchTime);

                busRepository.save(bus);
            }
        }
    }
}
