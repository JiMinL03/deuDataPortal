package deu.rest.api.Crawling;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;


import java.time.Duration;
import java.util.List;

public class SearchPage {
    private WebDriver driver;
    private String baseUrl;

    public SearchPage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
    }

    public void searchKeyword(String keyword) {
        driver.get(baseUrl);
        WebElement inputBox = driver.findElement(By.name("query")); // 검색창 이름 확인 필요
        inputBox.sendKeys(keyword);
        inputBox.sendKeys(Keys.ENTER);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".board_list li")));
    }

    public List<WebElement> getSearchContents() {
        return driver.findElements(By.cssSelector(".board_list li"));
    }

    public List<WebElement> getSearchLinks() {
        return driver.findElements(By.cssSelector(".board_list li a"));
    }
}
