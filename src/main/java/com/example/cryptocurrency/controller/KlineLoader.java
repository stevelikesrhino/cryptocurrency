package com.example.cryptocurrency.controller;

import com.example.cryptocurrency.service.KlineLoadService;
import com.example.cryptocurrency.service.ValidationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KlineLoader {
    private KlineLoadService klineLoadService;
    private ValidationService validationService;
    public KlineLoader(KlineLoadService klineLoadService, ValidationService validationService){
        this.klineLoadService = klineLoadService;
        this.validationService = validationService;
    }

    @PostMapping("/loadKline")
    public String loadKline(@RequestParam("symbol") String symbol,
                            @RequestParam("startTime") String startTime,
                            @RequestParam("endTime") String endTime){
        Long longStartTime = Long.parseLong(startTime);
        Long longEndTime = Long.parseLong(endTime);

        //validation symbol, startTime >= endTime throw specific exception
        validationService.timeValidation(longStartTime, longEndTime);

        validationService.symbolValidation(symbol);

        klineLoadService.load(symbol, longStartTime, longEndTime);
        return "loading complete";
    }

}
