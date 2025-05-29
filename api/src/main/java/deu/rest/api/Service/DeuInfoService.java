package deu.rest.api.Service;

import deu.rest.api.Entity.DeuInfo;
import deu.rest.api.Repository.DeuInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeuInfoService {

    private final DeuInfoRepository deuInfoRepository;

    public void fetchAndSaveDeuInfo() throws IOException {
        Document doc = Jsoup.connect("https://www.deu.ac.kr/www/index.do").get();

        Elements nameElement = doc.select("div.ft-logo");
        String schoolName = nameElement.text();

        Elements infoElements = doc.select("address.ad p span");
        String address = infoElements.get(0).text();
        String tel = infoElements.get(1).text();

        log.info("학교명: {}", schoolName);
        log.info("주소: {}", address);
        log.info("전화번호: {}", tel);

        if (!deuInfoRepository.existsByDeuName(schoolName)) {
            DeuInfo deuInfo = new DeuInfo();
            deuInfo.setDeuName(schoolName);
            deuInfo.setDeuAddress(address);
            deuInfo.setDeuPhone(tel);

            deuInfoRepository.save(deuInfo);
            log.info("DeuInfo 저장 완료");
        } else {
            log.info("이미 존재하는 학교 정보입니다.");
        }
    }
}
