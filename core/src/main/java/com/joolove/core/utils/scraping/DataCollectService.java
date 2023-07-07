package com.joolove.core.utils.scraping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DataCollectService {

    private final SeleniumComponent seleniumComponent;

    public void getAlcoholDataList() {
        seleniumComponent.process();
    }

}
