package deu.rest.api.Service;

import deu.rest.api.Entity.College;
import deu.rest.api.Entity.Department;
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

@Service
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
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

    public void fetchAndSaveDepartment() throws IOException { //공통 메서드(학과 이름, 학과 전화번호) 호출
        Document doc = Jsoup.connect("https://www.deu.ac.kr/www/deu-college.do").get();

        Elements subjectElements = doc.select("div.subject");
        Elements callElements = doc.select("div.call");

        saveDepartmentNames(subjectElements);
        saveDepartmentPhones(callElements);
    }

    private void saveDepartmentNames(Elements elements) {
        for (Element element : elements) {
            String name = element.text().trim();
            log.info("단과대학 name: {}", name);

            if (!departmentRepository.existsByDepartmentName(name)) {
                Department department = new Department();
                department.setDepartmentName(name);
                departmentRepository.save(department);
            }
        }
    }

    private void saveDepartmentPhones(Elements elements) {
        for (Element element : elements) {
            String phone = element.text().trim();
            log.info("단과대학 phone: {}", phone);

            if (!departmentRepository.existsByDepartmentPhone(phone)) {
                Department department = new Department();
                department.setDepartmentPhone(phone);
                departmentRepository.save(department);
            }
        }
    }

    private void fetchAndSaveTuition(){ //학과별 등록금 저장 로직 구현 필요
        
    }
}
