package com.smallcase.controller;

import com.smallcase.dto.FetchTradeResponse;
import com.smallcase.dto.TradeDTO;
import com.smallcase.services.TradeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/trades")
public class TradeController {

    @Autowired
    TradeService tradeService;

    @ApiOperation(value = "Create new trade entry", response = TradeDTO.class)
    @PostMapping(produces = "application/json")
    public TradeDTO createTrade(@RequestBody TradeDTO tradeDTO) {
        try {
            return tradeService.createTradeRecords(tradeDTO);
        } catch (Exception e) {
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

    @ApiOperation(value = "Retrieve all trades", response = FetchTradeResponse.class)
    @GetMapping(produces = "application/json")
    public FetchTradeResponse getTrades(@RequestParam(value = "userId") Long userId,
                                        @RequestParam(value = "tradeIds", required = false) List<Long> tradeIds) {
        try {
            return tradeService.getTradeRecords(userId, tradeIds);
        } catch (Exception e) {
            return FetchTradeResponse.builder().message("Error").success(false).build();
        }
    }

    @ApiOperation(value = "Update trade entry", response = TradeDTO.class)
    @PutMapping(produces = "application/json")
    public TradeDTO updateTrade(@RequestBody TradeDTO tradeDTO) {
        try {
            return tradeService.updateTradeRecord(tradeDTO);
        } catch (Exception e) {
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

    @ApiOperation(value = "Delete a trade entry", response = TradeDTO.class)
    @DeleteMapping(produces = "application/json")
    public TradeDTO deleteTrade(@RequestBody TradeDTO tradeDTO) {
        try {
            return tradeService.deleteTradeRecord(tradeDTO);
        } catch (Exception e) {
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

}
