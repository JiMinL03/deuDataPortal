package deu.rest.api.Service;

import deu.rest.api.Entity.College;
import deu.rest.api.Entity.Department;
import deu.rest.api.Entity.Tution;
import deu.rest.api.Repository.CollegeRepository;
import deu.rest.api.Repository.DepartmentRepository;
import deu.rest.api.Repository.TutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TutionService {
    private final TutionRepository tutionRepository;
    private final CollegeRepository collegeRepository;
    private final DepartmentRepository departmentRepository;

    private Document fetchDepartmentPage() throws IOException {
        return Jsoup.connect("https://ipsi.deu.ac.kr/submenu.do?menuUrl=54Mm3BQ0IOZq%2B7x%2BQeXT8w%3D%3D&").get();
    }

    private String cleanDepartmentName(String rawName) {
        return rawName.replaceAll("[^가-힣A-Za-z0-9·]", "").trim();
    }

    private void processTuitionRow(Element row) {
        Elements tds = row.select("td");
        if (tds.size() < 3) {
            log.warn("등록금 데이터 누락");
            return;
        }

        String collegeNamesRaw = tds.get(0).text();
        String tuitionFreshman1stRaw = tds.get(1).text();
        String tuitionAfterRaw = tds.get(2).text();

        // 첫 번째 항목만 사용
        String collegeNameRaw = collegeNamesRaw.split(",")[0];
        String collegeName = cleanDepartmentName(collegeNameRaw);

        Optional<College> collegeOpt = collegeRepository.findByName(collegeName);
        if (collegeOpt.isEmpty()) {
            log.warn("단과대학 없음: {}", collegeName);
            return;
        }
        College college = collegeOpt.get();

        boolean exists = tutionRepository.existsByCollege(college);
        if (exists) {
            log.info("이미 등록금 데이터 있음 - 단과대학: {}", collegeName);
            return;
        }

        Tution tution = new Tution();
        tution.setCollege(college);
        tution.setTuitionFreshman1st(tuitionFreshman1stRaw.trim());
        tution.setTuitionAfter(tuitionAfterRaw.trim());

        tutionRepository.save(tution);
        log.info("등록금 저장 완료 - 단과대학: {}, 1학년 1학기: {}, 이후: {}", collegeName, tuitionFreshman1stRaw, tuitionAfterRaw);
    }


    public void fetchAndSaveTutions() throws IOException {
        Document doc = fetchDepartmentPage();

        Elements tables = doc.select("div.contents-area");

        for (Element area : tables) {
            Element h4 = area.selectFirst("h4.title-type01");
            String title = h4 != null ? h4.text().trim() : "";

            Element table = area.selectFirst("table");
            if (table != null) {
                Elements rows = table.select("tbody > tr");
                for (Element row : rows) {
                    processTuitionRow(row);
                }
            }
        }
    }
}

