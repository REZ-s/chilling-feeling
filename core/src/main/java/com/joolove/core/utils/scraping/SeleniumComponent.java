package com.joolove.core.utils.scraping;

import com.joolove.core.domain.product.Category;
import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.product.GoodsDetails;
import com.joolove.core.domain.product.GoodsStats;
import com.joolove.core.repository.jpa.GoodsDetailsRepository;
import com.joolove.core.repository.jpa.GoodsStatsRepository;
import com.joolove.core.service.GoodsService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SeleniumComponent {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsDetailsRepository goodsDetailsRepository;
    @Autowired
    private GoodsStatsRepository goodsStatsRepository;

    private WebDriver driver;

    private static final String baseUrl = "https://business.veluga.kr/search/result/";

    private static final String detailUrl = "https://business.veluga.kr/drink/";

    private static final String pcUserName = "Althea";     // change this to your pc username

    private static final String chromeDriverPath = "C:\\Users\\" + pcUserName + "\\Desktop\\chromedriver.exe";

    private static final String filePath = "C:\\Users\\" + pcUserName + "\\Desktop\\collected_alcohol_data.txt";


    public void process() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");   // no browser
        options.addArguments("--disable-popup-blocking");   // no pop-up
        options.addArguments("--disable-gpu");  // no gpu
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--blink-settings=imagesEnabled=false");   // no image
        options.addArguments("--lang=ko");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*");
        options.setCapability("ignoreProtectedModeSettings", true);
        options.setCapability("browserVersion", "114");
        driver = new ChromeDriver(options);

        List<String[]> listForFile = new ArrayList<>();
        for (int i = 500; i <= 25778; ++i) {  // 24656 x 10 = 246.560 seconds?
            driver.get(detailUrl + i);    // open url

            try {
                Thread.sleep(10);
                collectDetailDataList(listForFile);
                System.out.println("collecting ... " + i);
                //writeToFile(listForFile);   // save to CSV file
            } catch (Exception e) {
                System.out.println("exception " + i);
                e.printStackTrace();
            }
        }

        System.out.println("all done!");
        driver.close(); // close tab
        driver.quit();  // close browser
    }

    private void collectDetailDataList(List<String[]> listForFile) throws Exception {
        if (driver.findElements(By.cssSelector("h1[class*='fzJCtd']")).size() == 0) {
            return; // empty page
        }

        String name = driver.findElement(By.cssSelector("h1[class*='fzJCtd']")).getText();
        String engName = driver.findElement(By.cssSelector("h2[class*='dKhEMP']")).getText();
        String type = null;
        String imageUrl = null;
        Short priceLevel = 1;
        String degree = null;
        String country = null;
        String company = null;
        String supplier = null;
        String color = null;
        String colorImgUrl = null;
        String description = null;
        String descImgUrlString = null;
        String summary = null;

        String aroma = null;
        String balance = null;
        String body = null;
        String tannin = null;
        String acidity = null;
        String sweetness = null;
        String soda = null;

        // 상품 이미지
        WebElement imageElement = driver.findElement(By.cssSelector("div[class*='fcdrym']:not([class*='border-radius-lg'])"));
        WebElement imgTag = imageElement.findElement(By.tagName("img"));
        imageUrl = imgTag.getAttribute("src");

        // 상품 상세 정보
        WebElement infoTable = driver.findElement(By.cssSelector("table[class*='VlgTable__StyledTable']"));
        List<WebElement> infoTableRows = infoTable.findElements(By.tagName("tr"));
        for (WebElement row : infoTableRows) {
            String thString = row.findElement(By.tagName("th")).getText();
            String tdString = row.findElement(By.tagName("td")).getText();

            switch (thString) {
                case "스타일" -> type = tdString;
                case "도수" -> degree = tdString.substring(0, tdString.length() - 1);
                case "국가/지역" -> country = tdString;
                case "제조사" -> company = tdString;
                case "공급사" -> supplier = tdString;
                //default -> throw new RuntimeException("Unknown type: " + thString);
                // 주요 품종 이라는 th가 가끔 있어서 주석 처리
            }
        }

        // 상품 설명
        if (driver.findElements(By.cssSelector("p[class*='bKhBRs']")).size() != 0) {
            WebElement descElement = driver.findElement(By.cssSelector("p[class*='bKhBRs']"));
            description = descElement.getText();
        }

        // 상품 설명 이미지
        if (driver.findElements(By.cssSelector("p[class*='fNrcnj']")).size() != 0) {
            WebElement descImgUrl = driver.findElement(By.cssSelector("p[class*='fNrcnj']"));
            descImgUrlString = descImgUrl.findElement(By.tagName("img")).getAttribute("src");
        }

        // 상품 특징
        if (driver.findElements(By.cssSelector("section[class*='iZxyTj']")).size() != 0) {
            List<WebElement> notes = driver.findElements(By.className("cbITeE"));

            for (WebElement element : notes) {
                WebElement header = element.findElement(By.className("gevTRh"));
                List<WebElement> textElements = element.findElements(By.className("eKsMbC"));

                if (header.getText().equals("색상")) {
                    colorImgUrl = element.findElement(By.cssSelector("div[class*='ContentCircle-']"))
                            .findElement(By.tagName("img")).getAttribute("src");
                    color = textElements.get(0).getText();

                } else if (header.getText().equals("아로마")) {
                    aroma = textElements.stream()
                            .map(WebElement::getText)
                            .collect(Collectors.joining(", "));

                } else if (header.getText().equals("팔레트")) {
                    for (WebElement e : element.findElements(By.cssSelector("div[class*='GraphBar']"))) {

                        // 팔레트의 구성 요소가 술마다 다르기 때문에 일치하는 성분에 따라 저장한다.
                        String paletteName = e.findElement(By.cssSelector("[class*='GraphName-']")).getText();

                        // 아래 style 의 margin-left 의 % 를 가져와 1, 2, 3, 4, 5 로 변환하면 된다.
                        WebElement styleElement = e.findElement(By.cssSelector("[class*='GraphWrap']"));

                        String levelString = "";

                        if (styleElement.findElements(By.cssSelector("[class*='cysSJn']")).size() != 0) {
                            levelString = "0";
                        }
                        else if (styleElement.findElements(By.cssSelector("[class*='fayzvN']")).size() != 0) {
                            levelString = "1";
                        }
                        else if (styleElement.findElements(By.cssSelector("[class*='iIrGFX']")).size() != 0) {
                            levelString = "2";
                        }
                        else if (styleElement.findElements(By.cssSelector("[class*='hKHveF']")).size() != 0) {
                            levelString = "3";
                        }
                        else {
                            levelString = "4";
                        }

                        switch (paletteName) {
                            case "탄산" -> soda = levelString;
                            case "바디" -> body = levelString;
                            case "타닌" -> tannin = levelString;
                            case "당도" -> acidity = levelString;
                            case "산미" -> sweetness = levelString;
                        }
                    }
                } else if (header.getText().startsWith("밸런스")) {
                    // 화살표의 각도에 따라 읽어서 저장해야하는데, 이거는 중요하지 않은 정보라서 넘긴다.
                } else {
                    throw new RuntimeException("Unknown type: " + header.getText());
                }
            }

            // 상품 요약
            if (driver.findElements(By.className("jmklTn")).size() != 0) {
                summary = driver.findElement(By.className("jmklTn")).getText();
            }
        }

        Goods goods = Goods.builder()
                .name(name)
                .salesStatus((short)1)
                .category(Category.builder()
                        .categoryName(type)
                        .build())
                .build();

        GoodsDetails goodsDetails = GoodsDetails.alcoholBuilder()
                .goods(goods)
                .name(name)
                .engName(engName)
                .type(type)
                .imageUrl(imageUrl)
                .degree(degree)
                .country(country)
                .company(company)
                .supplier(supplier)
                .color(color)
                .colorImageUrl(colorImgUrl)
                .description(description)
                .descriptionImageUrl(descImgUrlString)
                .summary(summary)
                .opt1Value(aroma)
                .opt2Value(balance)
                .opt3Value(body)
                .opt4Value(tannin)
                .opt5Value(acidity)
                .opt6Value(sweetness)
                .opt7Value(soda)
                .build();

        GoodsStats goodsStats = GoodsStats.builder()
                .goods(goods)
                .build();

        // DB 저장
        goodsStatsRepository.save(goodsStats);
        goodsDetailsRepository.save(goodsDetails);
        goodsService.addGoods(goods);

        // CSV 형태로 저장
/*        listForFile.add(new String[]{
                goodsDetails.getName(),
                goodsDetails.getEngName(),
                goodsDetails.getType(),
                goodsDetails.getImageUrl(),
                goodsDetails.getPriceLevel().toString(),
                goodsDetails.getDegree(),
                goodsDetails.getCountry(),
                goodsDetails.getCompany(),
                goodsDetails.getSupplier(),
                goodsDetails.getColor(),
                goodsDetails.getDescription(),
                goodsDetails.getSummary(),
                goodsDetails.getOpt1Value(),
                goodsDetails.getOpt2Value(),
                goodsDetails.getOpt3Value(),
                goodsDetails.getOpt4Value(),
                goodsDetails.getOpt5Value(),
                goodsDetails.getOpt6Value(),
                goodsDetails.getOpt7Value()
        });*/

    }

    private Goods buildGoodsByAlcoholDataDto(GoodsDetails dto) {
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
        if (data == null) {
            return "";
        }

        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
