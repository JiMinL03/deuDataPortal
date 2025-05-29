package deu.rest.api.Service;

import deu.rest.api.Entity.UserLink;
import deu.rest.api.Repository.UserLinkRepository;
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
public class UserLinkService {
    private final UserLinkRepository userLinkRepository;

    public void fetchAndSaveUserLink() throws IOException {
        log.info(">>> [UserLinkController] fetchColleges() 호출됨");

        Document doc = Jsoup.connect("https://www.deu.ac.kr/www/index.do").get();


        Elements serviceLinks = doc.select("div.info a.info-item");
        log.info(">>> 가져온 링크 수: {}", serviceLinks.size());

        for (Element link : serviceLinks) {
            String serviceName = link.text().trim();
            String serviceLink = link.absUrl("href").trim();

            log.info("서비스명: {}, 링크: {}", serviceName, serviceLink);

            if (!userLinkRepository.existsByServiceName(serviceName)) {
                UserLink userLink = new UserLink(); // 엔티티 객체 생성
                userLink.setServiceName(serviceName);
                userLink.setServiceLink(serviceLink);

                userLinkRepository.save(userLink); // 올바른 리포지토리로 저장
            }
        }
    }
}
