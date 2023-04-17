package com.joolove.core.utils.scraping;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeleniumComponent {
    private WebDriver driver;

    private static final String ALCOHOL_URL = "https://business.veluga.kr/search/result/?page=1";

    public void process() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Althea\\Desktop\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");   // no pop-up
        options.addArguments("headless");   // no browser
        options.addArguments("--disable-gpu");  // no gpu
        options.addArguments("--blink-settings=imagesEnabled=false");   // no image
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

        try {
            getDataList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.close(); // close tab
        driver.quit();  // close browser
    }

    private List<String> getDataList() throws InterruptedException {
        List<String> list = new ArrayList<>();

        driver.get(ALCOHOL_URL);
        Thread.sleep(1500);

        List<WebElement> elements = driver.findElements(By.cssSelector(".vlg-drink-card "));
        for (WebElement e : elements) {
            String name = e.findElement(By.className("drink-name")).getText();
            String type = e.findElement(By.className("drink-detail")).getText();

            System.out.println(type + " " + name);
        }

        return list;
    }
}
