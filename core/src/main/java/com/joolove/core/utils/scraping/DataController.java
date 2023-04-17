package com.joolove.core.utils.scraping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataController {

    private final DataCollectService dataCollectService;

/*    @GetMapping("/data_collect/alcohol")
    public List<AlcoholDataDto> getAlcoholDataList() {
        return dataCollectService.getAlcoholDataList();
    }*/

    @GetMapping("/data_collect/alcohol")
    public void getAlcoholDataList() {
        dataCollectService.getAlcoholDataList();
    }
}
