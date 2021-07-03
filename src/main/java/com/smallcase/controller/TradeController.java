package com.smallcase.controller;

import com.smallcase.dto.TradeDTO;
import com.smallcase.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/trades")
public class TradeController {

    @Autowired
    TradeService tradeService;

    @PostMapping(value = "/", produces = "application/json")
    public TradeDTO createTrade(@RequestBody TradeDTO tradeDTO) {
        try {
            return tradeService.createTradeRecords(tradeDTO);
        } catch (Exception e) {
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

    @GetMapping(value = "/", produces = "application/json")
    public TradeDTO getTrades() {
        try {
            return tradeService.getTradeRecords();
        } catch (Exception e) {
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

    @PutMapping(value = "/", produces = "application/json")
    public TradeDTO updateTrade(@RequestBody @Valid TradeDTO tradeDTO) {
        try {
            return tradeService.updateTradeRecord(tradeDTO);
        } catch (Exception e) {
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

    @DeleteMapping(value = "/", produces = "application/json")
    public TradeDTO deleteTrade(@RequestBody TradeDTO tradeDTO) {
        try {
            return tradeService.deleteTradeRecord(tradeDTO);
        } catch (Exception e) {
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

}
