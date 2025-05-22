package deu.rest.api.Service;

import deu.rest.api.Entity.College;
import deu.rest.api.Repository.CollegeRepository;
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
public class CollegeService {
    private final CollegeRepository collegeRepository;
    private final College college = new College();
    public void fetchAndSaveColleges() throws IOException { //단과대학 정보를 가져오고 저장하는 메서드
        Document doc = Jsoup.connect("https://www.deu.ac.kr/www/deu-college.do").get(); // Jsoup 라이브러리를 사용해 웹 페이지를 크롤링
        /*
        * Jsoup.connect(...) 주어진 URL에 HTTP 연결을 시도
        * .get() → 해당 웹페이지의 HTML 문서 전체를 가져와서 파싱한 후, Document 객체로 반환
        * Document doc → 웹페이지의 HTML DOM 전체 구조를 담고 있는 객체
        * */
        Elements collegeElements = doc.select("h4.h4-tit");
        /*DOM 객체들 중 h4 태그의 클래스 이름 h4-tit을 collegeElements에 저장한다.
        * */

        for (Element element : collegeElements) {
            String name = element.text().trim();
            log.info("단과대학 name: {}", name);

            if (!collegeRepository.existsByName(name)) {
                College college = new College(); //반복문 안에서 새 객체 생성
                college.setName(name);
                collegeRepository.save(college);
            }
        }
    }
}
