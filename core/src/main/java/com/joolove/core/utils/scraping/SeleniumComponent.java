package com.joolove.core.utils.scraping;

import com.joolove.core.domain.base_item.BaseAlcoholImpl;
import com.joolove.core.domain.product.Category;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.security.service.GoodsService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.Duration.ofMillis;

@Component
public class SeleniumComponent {
    private WebDriver driver;

    @Autowired
    private GoodsService goodsService;

    private static final String baseUrl = "https://business.veluga.kr/search/result/";

    private static final String detailUrl = "https://business.veluga.kr/drink/";

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

        for (int i = 1; i <= 24656; ++i) {
            driver.get(detailUrl + i);    // open url

            try {
                Thread.sleep(100);
                collectDetailDataList();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        driver.close(); // close tab
        driver.quit();  // close browser
    }

    private void collectDetailDataList() throws InterruptedException {
        List<Goods> goodsList = new ArrayList<>();
        List<String[]> listForFile = new ArrayList<>();

        String name = driver.findElement(By.cssSelector("h1[class*=DrinkName__Name]")).getText();
        String engName = driver.findElement(By.cssSelector("h2[class*=DrinkName__EnglishName]")).getText();
        String type = "";
        String imageUrl = "";
        Short priceLevel = 1;
        String degree = "";
        String country = "";
        String company = "";
        String supplier = "";
        String color = "";
        String description = "";
        String summary = "";

        String aroma = "";
        short soda = 0;
        short body = 0;
        short tannin = 0;
        short acidity = 0;
        short sweetness = 0;

        // 상품 이미지
        WebElement imageElement = driver.findElement(By.cssSelector("div[class*=vlg-drink-img-chip]:not([class*='border-radius-lg'])"));
        WebElement imgTag = imageElement.findElement(By.tagName("img"));
        imageUrl = imgTag.getAttribute("src");

        // 상품 상세 정보
        WebElement infoTable = driver.findElement(By.cssSelector("table[class*=VlgTable__StyledTable]"));
        List<WebElement> infoTableRows = infoTable.findElements(By.tagName("tr"));
        for (WebElement row : infoTableRows) {
            String thString = row.findElement(By.tagName("th")).getText();
            String tdString = row.findElement(By.tagName("td")).getText();

            switch (thString) {
                case "스타일" -> type = tdString;
                case "도수" -> degree = tdString;
                case "국가/지역" -> country = tdString;
                case "제조사" -> company = tdString;
                case "공급사" -> supplier = tdString;
                default -> throw new RuntimeException("Unknown type: " + thString);
            }
        }

        // 상품 설명
        WebElement descElement = driver.findElement(By.cssSelector("p[class*=DrinkDescription__Description]"));
        description = descElement.getText();

        // 상품 특징
        if (driver.findElements(By.className("vlg-tasting-note")).size() != 0) {
            WebElement featElement = driver.findElement(By.className("vlg-tasting-note"));
            List<WebElement> notes = featElement.findElements(By.className("tasting-note-item"));

            for (WebElement element : notes) {
                WebElement header = element.findElement(By.className("header"));
                List<WebElement> textElements = element.findElements(By.className("text-wrap"));

                if (header.getText().equals("색상")) {
                    color = textElements.get(0).getText();
                } else if (header.getText().equals("아로마")) {
                    aroma = textElements.stream()
                            .map(WebElement::getText)
                            .collect(Collectors.joining(", "));
                } else if (header.getText().equals("팔레트")) {
                    for (WebElement e : element.findElements(By.className("graph-bar"))) {
                        // 팔레트의 구성 요소가 술마다 다르기 때문에 일치하는 성분에 따라 저장한다.
                        String paletteName = e.findElement(By.className("name-wrap")).getText();

                        // 아래 style 의 margin-left 의 % 를 가져와 1, 2, 3, 4, 5 로 변환하면 된다.
                        String styleString = e.findElement(By.className("graph-wrap")).findElement(By.className("value")).getAttribute("style").intern();
                        String levelString = styleString.substring(styleString.indexOf("margin-left:") + 13, styleString.indexOf("%"));
                        short levelShort = Short.parseShort(levelString);

                        switch (paletteName) {
                            case "탄산" -> soda = levelShort;
                            case "바디" -> body = levelShort;
                            case "타닌" -> tannin = levelShort;
                            case "당도" -> acidity = levelShort;
                            case "산미" -> sweetness = levelShort;
                        }

                    }
                } else if (header.getText().startsWith("밸런스")) {
                    // 화살표의 각도에 따라 읽어서 저장해야하는데, 이거는 맨 나중에 하자.
                } else {
                    throw new RuntimeException("Unknown type: " + header.getText());
                }
            }

            // 상품 요약
            if (driver.findElements(By.className("drink-summary")).size() != 0) {
                summary = featElement.findElement(By.className("drink-summary")).getText();
            }
        }

        // DTO 생성
        // 여기에서 분류에 따라 별도로 다운캐스팅 해서 만들어야한다.
        BaseAlcoholImpl dto = BaseAlcoholImpl.fullBuilder()
                .name(name)
                .engName(engName)
                .type(type)
                .imageUrl(imageUrl)
                .priceLevel(priceLevel)
                .degree(degree)
                .country(country)
                .company(company)
                .supplier(supplier)
                .color(color)
                .description(description)
                .build();

        // 한 페이지당 1개의 상품이기때문에 buildGoodsByAlcoholDataDto(dto) 로 만들고 저장하면된다.
        Goods goods = buildGoodsByAlcoholDataDto(dto);

        // DB 저장
        goodsService.addGoods(goods);

        // CSV File 저장
        writeToFile(listForFile);
    }

    private Goods buildGoodsByAlcoholDataDto(BaseAlcoholImpl dto) {
        return Goods.builder()
                .name(dto.getName())
                .salesStatus((short)1)
                .category(
                        Category.builder()
                                .categoryName(dto.getType())
                                .build()
                )
                .build();
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
