package deu.rest.api.Service;

import deu.rest.api.Entity.Building;
import deu.rest.api.Entity.CampusMap;
import deu.rest.api.Repository.CampusMapRepository;
import deu.rest.api.Repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampusMapService {

    private final BuildingRepository buildingRepository;
    private final CampusMapRepository campusMapRepository;

    public void fetchAndSaveCampusMap() throws IOException {
        Document doc = Jsoup.connect("https://www.deu.ac.kr/www/deu-campus-map.do").get();
        Elements buildingSections = doc.select("div#campus-tab-cont .tab-panel");

        for (Element section : buildingSections) {
            String buildingName = section.select(".subject").text().replaceAll("^\\d+", "").trim();
            String content = section.select(".campus-item-info > p").text();

            // 기본 초기화
            String etc = "", one = "", two = "", three = "", four = "", five = "";
            String six = "", seven = "", eight = "", nine = "";

            Elements listItems = section.select(".campus-item-list ol li");
            for (Element item : listItems) {
                String label = item.select("span.num").text().toUpperCase();
                String value = item.ownText().trim();

                switch (label) {
                    case "1F": one = value; break;
                    case "2F": two = value; break;
                    case "3F": three = value; break;
                    case "4F": four = value; break;
                    case "5F": five = value; break;
                    case "6F": six = value; break;
                    case "7F": seven = value; break;
                    case "8F": eight = value; break;
                    case "9F": nine = value; break;
                    case "참고": etc = value; break;
                    default: break;
                }
            }

            // 건물명 기준으로 Building 조회 (없으면 스킵)
            Optional<Building> optionalBuilding = buildingRepository.findByBuildingName(buildingName);
            if (optionalBuilding.isPresent()) {
                CampusMap map = new CampusMap();
                map.setBuildingId(optionalBuilding.get());
                map.setContent(content);
                map.setEtc(etc);
                map.setOne_layer(one);
                map.setTwo_layer(two);
                map.setThree_layer(three);
                map.setFour_layer(four);
                map.setFive_layer(five);
                map.setSix_layer(six);
                map.setSeven_layer(seven);
                map.setEight_layer(eight);
                map.setNine_layer(nine);

                campusMapRepository.save(map);
                log.info("저장 완료: {}", buildingName);
            } else {
                log.warn("건물 정보 없음: {}", buildingName);
            }
        }
    }
}
