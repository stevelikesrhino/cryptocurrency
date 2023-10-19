package com.example.cryptocurrency.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)

public class Kline {
    @NotNull
    private Long openTime;
    private Long closeTime;
    @NotBlank
    private String symbol;
    @Min(0)
    private BigDecimal openPrice;
    @Min(0)
    private BigDecimal highPrice;
    @Min(0) private BigDecimal lowPrice;
    @Min(0) private BigDecimal volume;
    private BigDecimal quoteAssetVolume;
    @Min(0)
    private int numberOfTrades;
    private BigDecimal takerBuyBaseAssetVolume;
    private BigDecimal takerBuyQuoteAssetVolume;
    private BigDecimal closePrice;
}
