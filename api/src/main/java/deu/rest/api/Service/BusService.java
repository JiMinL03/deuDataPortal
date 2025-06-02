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
    public void fetchAndSaveBus() throws IOException {
        Document doc = Jsoup.connect("https://www.deu.ac.kr/www/deu-bus.do").get();

        // 셔틀버스 정보 각각의 item 블록을 선택
        Elements busItems = doc.select("div.sub-bus > div.item");

        for (Element item : busItems) {
            // 버스 이름
            String busName = item.selectFirst("div.subject > span").text().trim();

            // 노선 링크와 텍스트
            Element routeElement = item.selectFirst("div.info dl:has(dt:contains(노선)) dd a.txt-under");
            String route = routeElement != null ? routeElement.text().trim() : "";

            // 첫차
            Element firstBusElement = item.selectFirst("div.info dl:has(dt:contains(첫차)) dd");
            String firstBus = firstBusElement != null ? firstBusElement.text().trim() : "";

            // 막차
            Element lastBusElement = item.selectFirst("div.info dl:has(dt:contains(막차)) dd");
            String lastBus = lastBusElement != null ? lastBusElement.text().trim() : "";

            // 배차간격
            Element dispatchElement = item.selectFirst("div.info dl:has(dt:contains(배차간격)) dd");
            String dispatchTime = dispatchElement != null
                    ? dispatchElement.html()
                    .replaceAll("<br>", " ")        // <br> → 공백으로 바꾸기
                    .replaceAll("<.*?>", "")        // 나머지 태그 제거
                    .replaceAll("\\s+", " ")        // 연속 공백/줄바꿈도 한 칸 공백으로 치환
                    .trim()
                    : "";

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
