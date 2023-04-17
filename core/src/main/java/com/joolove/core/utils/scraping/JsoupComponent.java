package com.joolove.core.utils.scraping;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class JsoupComponent {

    private static final String ALCOHOL_URL = "https://business.veluga.kr/search/result/?page=1";

    public List<AlcoholDataDto> getAlcoholDataList() {
        Connection conn = Jsoup.connect(ALCOHOL_URL);

        try {
            Document document = conn.get();
            return getAlcoholDataList(document);
        } catch (IOException ignored) {
        }

        return null;
    }

    public List<AlcoholDataDto> getAlcoholDataList(Document document) {
        Elements elements = document.select(".vlg-drink-card ");
        List<AlcoholDataDto> list = new ArrayList<>();

        for (Element element : elements) {
            list.add(createAlcoholDataDto(element.select("a")));
        }

        return list;
    }

    public AlcoholDataDto createAlcoholDataDto(Elements e) {
        AlcoholDataDto alcoholDataDto = AlcoholDataDto.builder().build();
        Class<?> aClass = alcoholDataDto.getClass();
        Field[] fields = aClass.getDeclaredFields();

        for (int i = 0; i < e.size(); i++) {
            String text;

            // (1) 술 종류
            String type = e.get(i).select("span.drink-detail text-overflow").text();

            // (2) 술 이름
            String name = e.get(i).select("span.drink-name text-overflow").text();

            text = type + " " + name;
            fields[i].setAccessible(true);
            try {
                fields[i].set(alcoholDataDto, text);
            } catch (Exception ignored) {
            }
        }

        return alcoholDataDto;
    }

    // DB에 데이터 저장 메소드
    public void saveAlcoholDataList(List<AlcoholDataDto> list) {
        for (AlcoholDataDto alcoholDataDto : list) {
            // dto -> entity
            //alcoholDataRepository.save(alcoholData);
        }
    }
}