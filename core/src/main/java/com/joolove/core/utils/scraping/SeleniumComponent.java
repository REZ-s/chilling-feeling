package com.joolove.core.utils.scraping;

import com.joolove.core.domain.product.Category;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.security.service.GoodsService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeleniumComponent {
    private WebDriver driver;

    @Autowired
    private GoodsService goodsService;

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
        driver.get(ALCOHOL_URL);    // open url

        try {
            Thread.sleep(1000);
            createDataList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.close(); // close tab
        driver.quit();  // close browser
    }

    private void createDataList() throws InterruptedException {
        List<Goods> goodsList = new ArrayList<>();
        List<WebElement> elements = driver.findElements(By.cssSelector(".vlg-drink-card "));

        for (WebElement element : elements) {
            String name = element.findElement(By.className("drink-name")).getText();
            String type = element.findElement(By.className("drink-detail")).getText();

            // AlcoholDataDto 생성
            AlcoholDataDto alcoholDataDto = AlcoholDataDto.builder()
                    .name(name)
                    .type(type)
                    .build();

            // Goods 추가
            goodsList.add(buildGoodsByAlcoholDataDto(alcoholDataDto));
        }

        // DB에 저장
        goodsService.addGoodsList(goodsList);
    }

    private Goods buildGoodsByAlcoholDataDto(AlcoholDataDto dto) {
        return Goods.builder()
                .name(dto.getName())
                .salesStatus((short)1)
                .category(
                        Category.builder()
                                .categoryName(dto.getType())
                                .build()
                )
                .build();

        // 상세 설명, 가격 등은 스크래핑 사이트에서 클릭하여 세부정보로 이동해서 받아와야함
    }
}
