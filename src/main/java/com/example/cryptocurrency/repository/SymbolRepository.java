package com.example.cryptocurrency.repository;

import com.example.cryptocurrency.model.Kline;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.apache.ibatis.annotations.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper
@Validated
public interface SymbolRepository {
    @Select("select * from candlestick_data")
    public List<Kline> findAll();



    @Insert({"<script>"+
            "insert into candlestick_data(open_time, close_time, symbol, open_price, high_price, low_price," +
                    "volume, quote_asset_volume, number_of_trades, taker_buy_base_asset_volume, " +
                    "taker_buy_quote_asset_volume, close_price)"+
            "values "+
            "<foreach collection='klineList' item='kline' separator=','> "+
                    "(#{kline.openTime}, #{kline.closeTime}, #{kline.symbol}, #{kline.openPrice}, " +
                    "#{kline.highPrice}, #{kline.lowPrice}, #{kline.volume}, " +
                    "#{kline.quoteAssetVolume}, #{kline.numberOfTrades}, #{kline.takerBuyBaseAssetVolume}, " +
                    "#{kline.takerBuyQuoteAssetVolume}, #{kline.closePrice})"+
            "</foreach>"+
            "</script>"
    })
    public int insert(@Param("klineList") @NotEmpty List< @Valid Kline> klineList);

    @Update("Update candlestick_data set open_price=#{openPrice}," +
            "high_price=#{highPrice}, low_price=#{lowPrice}, volume=#{volume}, quote_asset_volume#{quoteAssetVolume}, " +
            "number_of_trades=#{numberOfTrades}, taker_buy_base_asset_volume=#{takerBuyBaseAssetVolume}, " +
            "taker_buy_quote_asset_volume=#{takerBuyQuoteAssetVolume}, close_price=#{closePrice} where " +
            "open_time=#{openTime}, close_time=#{closeTime}, symbol=#{symbol}")
    public int update(Kline kline);

    @Select("select * from candlestick_data where open_time >= #{startTime} and close_time <= #{endTime} " +
            "and symbol=#{symbol}")
    public List<Kline> findByPrimaryKey(@Param("startTime") Long startTime, @Param("endTime") Long endTime,
                                        @Param("symbol") String symbol);
}
