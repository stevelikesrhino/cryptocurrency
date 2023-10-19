package com.example.cryptocurrency.controller;

import com.example.cryptocurrency.model.Kline;
import com.example.cryptocurrency.service.KlineRetrieveService;
import com.example.cryptocurrency.service.ValidationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KlineRetriever {
    private KlineRetrieveService klineRetrieveService;
    private ValidationService validationService;

    public KlineRetriever(KlineRetrieveService klineRetrieveService, ValidationService validationService){
        this.klineRetrieveService = klineRetrieveService;
        this.validationService = validationService;
    }

    @GetMapping("/retrieveKlines")
    public String retrieveKlines(@RequestParam String symbol,
                                 @RequestParam Long startTime,
                                 @RequestParam Long endTime,
                                 @RequestParam int interval,
                                 @RequestParam (value = "limit", required = false) Integer limit
                                 ) {
        // interval parameter is in minutes, convert to ms
        Long msInterval = interval * 60L * 1000L;
        List<Kline> convertedList;
        if(limit == null) {
            convertedList = klineRetrieveService.convertToHigherInterval(symbol, startTime, endTime, msInterval);
        } else {
            convertedList = klineRetrieveService.convertToHigherInterval(symbol, startTime, endTime, msInterval, limit);
        }
        return convertedList.toString();
    }
}
