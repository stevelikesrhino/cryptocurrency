package com.example.cryptocurrency.service;

import com.example.cryptocurrency.model.Kline;
import com.example.cryptocurrency.repository.SymbolRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

@Service
@Validated
public class KlineLoadService {

    @Autowired
    private SymbolRepository symbolRepository;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${api.size.limit}")
    private Integer sizeLimit;

    @Autowired
    private BinanceApiService service;

    private static final Logger logger = LoggerFactory.getLogger(KlineLoadService.class);

    public void load(@NotBlank String symbol, @NotNull Long startTime, @NotNull Long endTime) {
        Long sizeLimitMillSeconds = sizeLimit * 60 * 1000L;
        // Parallel LongStream
        LongStream.range(startTime, endTime)
                .parallel()
                .filter(s -> (s - startTime) % sizeLimitMillSeconds == 0)
                .forEach(s -> this.loadIndividual(symbol, s, Math.min(s + sizeLimitMillSeconds - 1L, endTime - 1L)));
    }


    private void loadIndividual(String symbol, Long newStart, Long newEnd) {
        String[][] body = service.load(symbol, newStart, newEnd);
        List<Kline> klineList = Arrays.stream(body)
                .parallel()
                .map(stringArr -> this.convert(stringArr, symbol))
                .toList();
        symbolRepository.insert(klineList);
    }

    private Kline convert(String[] klineArr, String symbol) {
        return new Kline()
                .setOpenTime(Long.parseLong(klineArr[0])) // string to long
                .setOpenPrice(new BigDecimal(klineArr[1]))
                .setHighPrice(new BigDecimal(klineArr[2]))
                .setLowPrice(new BigDecimal(klineArr[3]))
                .setClosePrice(new BigDecimal(klineArr[4]))
                .setVolume(new BigDecimal(klineArr[5]))
                .setCloseTime(Long.parseLong(klineArr[6]))
                .setQuoteAssetVolume(new BigDecimal(klineArr[7]))
                .setNumberOfTrades(Integer.parseInt(klineArr[8]))
                .setTakerBuyBaseAssetVolume(new BigDecimal(klineArr[9]))
                .setTakerBuyQuoteAssetVolume(new BigDecimal(klineArr[10]))
                .setSymbol(symbol);
    }


}
