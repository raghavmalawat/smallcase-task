package com.smallcase.controller;

import com.smallcase.dto.FetchTradeResponse;
import com.smallcase.dto.TradeDTO;
import com.smallcase.enums.SecurityType;
import com.smallcase.enums.TradeType;
import com.smallcase.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trades")
public class TradeController {

    @Autowired
    TradeService tradeService;

    @PostMapping(produces = "application/json")
    public TradeDTO createTrade(@RequestBody TradeDTO tradeDTO) {
        try {
            return tradeService.createTradeRecords(tradeDTO);
        } catch (Exception e) {
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

    @GetMapping(produces = "application/json")
    public FetchTradeResponse getTrades(@RequestParam(value = "userId") Long userId,
                                        @RequestParam(value = "tradeIds", required = false) List<Long> tradeIds) {
        try {
            return tradeService.getTradeRecords(userId, tradeIds);
        } catch (Exception e) {
            return FetchTradeResponse.builder().message("Error").success(false).build();
        }
    }

    @PutMapping(produces = "application/json")
    public TradeDTO updateTrade(@RequestBody TradeDTO tradeDTO) {
        try {
            return tradeService.updateTradeRecord(tradeDTO);
        } catch (Exception e) {
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

    @DeleteMapping(produces = "application/json")
    public TradeDTO deleteTrade(@RequestBody TradeDTO tradeDTO) {
        try {
            return tradeService.deleteTradeRecord(tradeDTO);
        } catch (Exception e) {
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

}
