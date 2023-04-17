package com.joolove.core.utils.scraping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataCollectService {
    private final JsoupComponent jsoupComponent;
    private final SeleniumComponent seleniumComponent;

    public void getAlcoholDataList() {
        seleniumComponent.process();
    }

}
