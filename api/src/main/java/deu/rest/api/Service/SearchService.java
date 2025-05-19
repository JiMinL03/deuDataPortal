package deu.rest.api.Service;

import deu.rest.api.Crawling.SearchPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    public List<String> performSearch(String keyword) {
        WebDriver driver = new ChromeDriver();
        List<String> resultTexts = new ArrayList<>();

        try {
            String url = "https://www.deu.ac.kr/www/search.do";
            SearchPage searchPage = new SearchPage(driver, url);

            searchPage.searchKeyword(keyword);
            List<WebElement> contents = searchPage.getSearchContents();

            for (WebElement element : contents) {
                resultTexts.add(element.getText().strip());
            }

            return resultTexts;

        } finally {
            driver.quit();
        }
    }
}
