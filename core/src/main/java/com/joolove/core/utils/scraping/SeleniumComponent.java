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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SeleniumComponent {
    private WebDriver driver;

    @Autowired
    private GoodsService goodsService;

    private static final String alcoholUrl = "https://business.veluga.kr/search/result/";

    private static final String chromeDriverPath = "C:\\Users\\Althea\\Desktop\\chromedriver.exe";

    private static final String filePath = "C:\\Users\\Althea\\Desktop\\collected_alcohol_data.txt";

    public void process() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");   // no pop-up
        options.addArguments("headless");   // no browser
        options.addArguments("--disable-gpu");  // no gpu
        options.addArguments("--blink-settings=imagesEnabled=false");   // no image
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get(alcoholUrl);    // open url

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
        List<String[]> listForFile = new ArrayList<>();
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
            listForFile.add(new String[]{ name, type });
        }

        // DB 저장
        goodsService.addGoodsList(goodsList);

        // CSV File 저장
        writeToFile(listForFile);
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

    public void writeToFile(List<String[]> dataLines) {
        File csvOutputFile = new File(filePath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
