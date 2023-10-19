package com.example.cryptocurrency.service;

import com.example.cryptocurrency.model.exception.InputInvalidException;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Service
@Validated
public class ValidationService {
    @Autowired
    private BinanceApiService service;

    private Map<String, Boolean> symbolMap;

    @PostConstruct
    public void post(){
        symbolMap = service.getSymbolMap();
    }

    public void symbolValidation(@NotBlank String symbol){
        if(symbolMap.get(symbol) == null){
            throw new InputInvalidException("Invalid symbol");
        } else {
            if(symbolMap.get(symbol) == false){
                throw new InputInvalidException("symbol " + symbol+" not trading");
            }
        }
    }

    public void timeValidation(@NotNull Long start, Long end){
        if(start >= end || start >= System.currentTimeMillis()
                || end >= System.currentTimeMillis()){
            throw new InputInvalidException();
        }
    }
}
