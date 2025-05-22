package deu.rest.api.Service;

import deu.rest.api.Entity.College;
import deu.rest.api.Entity.Department;
import deu.rest.api.Repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final Department department = new Department();

    public List<String> fetchMajorHomepages() throws IOException {
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
}
