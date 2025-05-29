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
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampusMapService {

    private final BuildingRepository buildingRepository;
    private final CampusMapRepository campusMapRepository;

    public void fetchAndSaveCampusMap() throws IOException {
        Document doc = Jsoup.connect("https://www.deu.ac.kr/www/deu-campus-map.do").get();
        Elements buildingSections = doc.select("div#campus-tab-cont .tab-panel");

        for (Element section : buildingSections) {
            // ✅ 건물 번호 및 이름 추출 (HTML 구조에 맞게)
            Element subjectElement = section.selectFirst(".subject");
            if (subjectElement == null) {
                log.warn("건물 정보 없음: subject 태그 없음");
                continue;
            }

            Element numElement = subjectElement.selectFirst(".num");
            if (numElement == null) {
                log.warn("건물 번호 추출 실패: {}", subjectElement.text());
                continue;
            }

            int buildingId;
            try {
                buildingId = Integer.parseInt(numElement.text().trim());
            } catch (NumberFormatException e) {
                log.warn("건물 번호 숫자 변환 실패: {}", numElement.text());
                continue;
            }

            String buildingName = subjectElement.ownText().trim();
            if (buildingName.isEmpty()) {
                log.warn("건물명 추출 실패: {}", subjectElement.text());
                continue;
            }

            // ✅ 설명 추출
            String content = section.select(".campus-item-info > p").text().trim();

            // ✅ Building 저장 (중복 방지)
            Building building = buildingRepository.findByBuildingId(buildingId).orElseGet(() -> {
                Building newBuilding = new Building();
                newBuilding.setBuildingId(buildingId);
                newBuilding.setBuildingName(buildingName);
                return buildingRepository.save(newBuilding);
            });

            // ✅ 층별 정보 파싱
            Map<String, String> floors = new HashMap<>();
            Elements listItems = section.select(".campus-item-list ol li");

            for (Element item : listItems) {
                String label = item.select("span.num").text().toUpperCase(); // 예: "1F"
                String value = item.ownText().trim();
                floors.put(label, value);
            }

            // ✅ CampusMap 저장
            CampusMap map = new CampusMap();
            map.setBuildingId(building);
            map.setContent(content);
            map.setEtc(floors.getOrDefault("참고", ""));
            map.setOne_layer(floors.getOrDefault("1F", ""));
            map.setTwo_layer(floors.getOrDefault("2F", ""));
            map.setThree_layer(floors.getOrDefault("3F", ""));
            map.setFour_layer(floors.getOrDefault("4F", ""));
            map.setFive_layer(floors.getOrDefault("5F", ""));
            map.setSix_layer(floors.getOrDefault("6F", ""));
            map.setSeven_layer(floors.getOrDefault("7F", ""));
            map.setEight_layer(floors.getOrDefault("8F", ""));
            map.setNine_layer(floors.getOrDefault("9F", ""));

            campusMapRepository.save(map);
            log.info("저장 완료: 건물명={}, 건물ID={}", buildingName, buildingId);
        }
    }
}
