package deu.rest.api.Service;

import deu.rest.api.Entity.Department;
import deu.rest.api.Entity.Professor;
import deu.rest.api.Repository.DepartmentRepository;
import deu.rest.api.Repository.ProfessorRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final DepartmentRepository departmentRepository;
    private final Professor professor = new Professor();

    public void crawlAllProfessors() throws IOException {

        Map<String, String> profLinksMap = fetchProfessorLinks();

        for (Map.Entry<String, String> entry : profLinksMap.entrySet()) {
            String departmentName = entry.getKey();
            String profUrl = entry.getValue();
            fetchAndSaveProfessors(profUrl, departmentName);
        }
    }

    public Map<String, String> fetchProfessorLinks() throws IOException {
        Map<String, String> profLinksMap = new HashMap<>();
        List<String> homepageLinks = fetchDepartmentHomepages();

        for (String url : homepageLinks) {
            try {
                Document doc = Jsoup.connect(url).get();

                String departmentName = doc.title();

                Elements profLinkElements = doc.select("a");

                for (Element a : profLinkElements) {
                    if (!a.text().contains("교수소개")) continue;

                    String profLink = a.absUrl("href");

                    if (profLink == null || profLink.isEmpty()
                            || profLink.equals("#")
                            || profLink.startsWith("javascript:")
                            || !profLink.startsWith("http")) {
                        continue;
                    }

                    profLinksMap.put(departmentName, profLink);
                }
            } catch (HttpStatusException e) {
                if (e.getStatusCode() == 404) {
                    System.out.println("404 Not Found 에러로 URL 건너뜀: " + url);
                    continue;  // 404 에러 난 URL 건너뜀
                } else {
                    throw e;  // 그 외 에러는 다시 던짐
                }
            }
        }

        return profLinksMap;
    }

    public List<String> fetchDepartmentHomepages() throws IOException {
        List<String> homepageLinks = new ArrayList<>();
        Document doc = Jsoup.connect("https://www.deu.ac.kr/www/deu-college.do").get();
        Elements linkElements = doc.select("a.btn-home"); // 또는 다른 셀렉터

        for (Element element : linkElements) {
            String href = element.absUrl("href");  // 절대 URL로 뽑기
            if (!href.isEmpty()) {
                homepageLinks.add(href);
            }
        }

        return homepageLinks;
    }
    public void fetchAndSaveProfessors(String professorPageUrl, String departmentName) throws IOException {
        Document doc = Jsoup.connect(professorPageUrl).get();

        Elements professorElements = doc.select("div.teachList ul li");

        for (Element prof : professorElements) {
            String name = prof.select("dl dt p").text().trim();
            if (name.isEmpty()) {
                // 이름이 없으면 데이터가 없거나 잘못된 정보일 가능성이 높으니 건너뜀
                continue;
            }

            String researchArea = prof.select("dl dt span").text().trim();

            Elements infoItems = prof.select("dl dd ul li");

            String lab = "";
            String phone = "";
            String email = "";

            for (Element item : infoItems) {
                String label = item.select("span.name") != null ? item.select("span.name").text().trim() : "";
                String value = item.ownText() != null ? item.ownText().trim() : "";

                if (label.contains("연구실") && !value.isEmpty()) {
                    lab = value;
                } else if (label.contains("연락처") && !value.isEmpty()) {
                    phone = value;
                } else if (label.contains("E-MAIL")) {
                    Element emailLink = item.selectFirst("a");
                    if (emailLink != null) {
                        String href = emailLink.attr("href");
                        if (href != null && href.startsWith("mailto:")) {
                            email = href.replace("mailto:", "").trim();
                        }
                    }
                }
            }

            Optional<Department> departmentOpt = departmentRepository.findByDepartmentName(departmentName);

            Professor professor = new Professor();
            professor.setName(name);
            professor.setResearchArea(researchArea);
            professor.setLab(lab);
            professor.setPhone(phone);
            professor.setEmail(email);

            if (departmentOpt.isPresent()) {
                professor.setDepartment(departmentOpt.get());
            } else {
                professor.setDepartment(null);
                System.out.println("Department not found for name: " + departmentName);
            }

            professorRepository.save(professor);
        }
    }
    //https://csw.deu.ac.kr/se/sub02.do
}
