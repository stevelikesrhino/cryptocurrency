package com.example.cryptocurrency.service;

import com.example.cryptocurrency.model.Kline;
import com.example.cryptocurrency.repository.SymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KlineRetrieveService {

    private SymbolRepository symbolRepository;

    @Autowired
    public KlineRetrieveService(SymbolRepository symbolRepository){
        this.symbolRepository = symbolRepository;
    }

    public List<Kline> retrieveKline(String symbol, Long startTime, Long endTime) {
        return symbolRepository.findByPrimaryKey(startTime, endTime, symbol);
    }

    public List<Kline> convertToHigherInterval(String symbol, Long startTime, Long endTime, Long interval){
        return convertToHigherInterval(symbol, startTime, endTime, interval, 1000);
    }

    public List<Kline> convertToHigherInterval(String symbol, Long startTime, Long endTime, Long interval, int limit) {
        if(limit > 1000) limit = 1000;
        List<Kline> allKlines = symbolRepository.findByPrimaryKey(startTime, endTime, symbol);
        Collection<List<Kline>> values = allKlines.stream()
                .parallel()
                .collect(Collectors.groupingBy(kline -> ((kline.getOpenTime() - startTime) / interval)))
                .values();

        return values.stream()
                .map(this::convertKlines)
                .sorted(Comparator.comparing(Kline::getOpenTime))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Kline convertKlines(List<Kline> klines) {
        Kline newKline = new Kline();

        newKline.setOpenTime(klines.get(0).getOpenTime());
        newKline.setCloseTime(klines.get(klines.size() - 1).getCloseTime());
        newKline.setSymbol(klines.get(0).getSymbol());
        newKline.setOpenPrice(klines.get(0).getOpenPrice());
        newKline.setClosePrice(klines.get(klines.size() - 1).getClosePrice());

        BigDecimal highPrice = klines.get(0).getHighPrice();
        BigDecimal lowPrice = klines.get(0).getLowPrice();
        BigDecimal volumeSum = new BigDecimal(0);
        Integer numberOfTradesSum = 0;
        BigDecimal takerBuyBaseAssetVolumeSum = new BigDecimal(0);
        BigDecimal takerBuyQuoteAssetVolumeSum = new BigDecimal(0);
        BigDecimal quoteAssetVolumeSum = new BigDecimal(0);

        for (Kline curr : klines) {
            if (curr.getHighPrice().compareTo(highPrice) > 0) {
                highPrice = curr.getHighPrice();
            }

            if (curr.getLowPrice().compareTo(lowPrice) < 0) {
                lowPrice = curr.getLowPrice();
            }

            numberOfTradesSum += curr.getNumberOfTrades();
            takerBuyBaseAssetVolumeSum = takerBuyBaseAssetVolumeSum.add(curr.getTakerBuyBaseAssetVolume());
            takerBuyQuoteAssetVolumeSum = takerBuyQuoteAssetVolumeSum.add(curr.getTakerBuyQuoteAssetVolume());
            volumeSum = volumeSum.add(curr.getVolume());
            quoteAssetVolumeSum = quoteAssetVolumeSum.add(curr.getQuoteAssetVolume());
        }

        newKline.setHighPrice(highPrice);
        newKline.setLowPrice(lowPrice);
        newKline.setVolume(volumeSum);
        newKline.setQuoteAssetVolume(quoteAssetVolumeSum);
        newKline.setNumberOfTrades(numberOfTradesSum);
        newKline.setTakerBuyBaseAssetVolume(takerBuyBaseAssetVolumeSum);
        newKline.setTakerBuyQuoteAssetVolume(takerBuyQuoteAssetVolumeSum);

        return newKline;
    }
}
