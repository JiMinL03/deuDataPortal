package deu.rest.api.Service;

import deu.rest.api.Entity.College;
import deu.rest.api.Entity.Department;
import deu.rest.api.Repository.CollegeRepository;
import deu.rest.api.Repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final CollegeRepository collegeRepository;
    private final Department department = new Department();

    public List<String> fetchMajorHomepages() throws IOException { //하위 학과 링크 출력
        List<String> homepageLinks = new ArrayList<>();

        Document doc = Jsoup.connect("https://www.deu.ac.kr/www/deu-college.do").get();
        Elements linkElements = doc.select("a.btn-home");  // class="btn-home"인 <a> 태그들 선택

        for (Element element : linkElements) {
            String href = element.attr("href");  // href 속성 추출
            if (!href.isEmpty()) {
                homepageLinks.add(href);
            }
        }

        return homepageLinks;
    }

    public void fetchAndSaveDepartment() throws IOException { //메서드 분리
        Document doc = fetchDepartmentPage();
        Elements collegeTitles = extractCollegeTitles(doc);

        for (Element collegeTitle : collegeTitles) {
            processCollegeBlock(collegeTitle);
        }
    }

    private Document fetchDepartmentPage() throws IOException {//HTML 페이지 가져오기
        return Jsoup.connect("https://www.deu.ac.kr/www/deu-college.do").get();
    }

    private Elements extractCollegeTitles(Document doc) { //단과대학 제목 요소 리스트 추출
        return doc.select("h4.h4-tit");
    }

    private void processCollegeBlock(Element collegeTitle) { //단과대학 하나에 대한 학과 처리
        String collegeName = collegeTitle.text().trim();
        Optional<College> collegeOpt = collegeRepository.findByName(collegeName);

        if (collegeOpt.isEmpty()) {
            log.warn("단과대학을 찾을 수 없음: {}", collegeName);
            return;
        }

        College college = collegeOpt.get();
        Element sibling = collegeTitle.nextElementSibling();

        if (sibling == null || !sibling.hasClass("item-btn-wrap")) {
            log.warn("학과 리스트를 찾을 수 없음: {}", collegeName);
            return;
        }

        Elements items = sibling.select("div.item");
        for (Element item : items) {
            processDepartmentItem(item, college);
        }
    }

    private void processDepartmentItem(Element item, College college) {//학과 한 행 저장 처리
        Element subject = item.selectFirst("div.subject");
        Element call = item.selectFirst("span.call");

        if (subject == null || call == null) {
            log.warn("누락된 데이터 - 단과대학: {}", college.getName());
            return;
        }

        String subjectName = subject.text().trim();
        String phone = call.text().trim();

        if (!departmentRepository.existsByDepartmentName(subjectName)) {
            Department department = new Department();
            department.setDepartmentName(subjectName);
            department.setDepartmentPhone(phone);
            department.setCollegeId(college);
            departmentRepository.save(department);

            log.info("저장됨 - 학과: {}, 전화: {}, 단과대: {}", subjectName, phone, college.getName());
        }
    }


}
