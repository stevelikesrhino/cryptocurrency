package com.example.cryptocurrency.service;

import com.example.cryptocurrency.model.exception.HTTPResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class BinanceApiService {
    private static final Logger logger = LoggerFactory.getLogger(BinanceApiService.class);
    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.size.limit}")
    private Integer sizeLimit;

    @Value("${api.exchangeInfoUrl}")
    private String exchangeInfoUrl;

    @Autowired
    private RestTemplate restTemplate;

    public String[][] load(String symbol, Long startTime, Long endTime){
        String url = String.format(apiUrl, symbol, startTime, endTime, sizeLimit);
        logger.info(String.format("url:%s", url));
        ResponseEntity<String[][]> response = restTemplate.getForEntity(url, String[][].class);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new HTTPResponseException("Bad HTTP response from the server");
        }
        return response.getBody();
    }

    public Map<String, Boolean> getSymbolMap(){
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseEntity<String> response = restTemplate.getForEntity(exchangeInfoUrl, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new HTTPResponseException("Bad HTTP response from the server");
            }
            JsonNode rootNode = null;
            try {
                rootNode = objectMapper.readTree(response.getBody());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            JsonNode symbolsNode = rootNode.get("symbols");
            Map<String, Boolean> symbolMap = new HashMap<>();
            // Get all the symbols from the symbols object
            Iterator<JsonNode> symbolsIterator = symbolsNode.elements();
            while (symbolsIterator.hasNext()) {
                JsonNode symbolNode = symbolsIterator.next();
                String symbol = symbolNode.get("symbol").asText();
                symbolMap.put(symbol, "TRADING".equals(symbolNode.get("status").asText()));
            }

            return symbolMap;
    }
}
