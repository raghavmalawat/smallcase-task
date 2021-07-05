package com.smallcase.controller;

import com.smallcase.LogFactory;
import com.smallcase.dto.FetchTradeResponse;
import com.smallcase.dto.TradeDTO;
import com.smallcase.services.TradeService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/trades")
public class TradeController {

    private static final Logger logger = LogFactory.getLogger(TradeController.class);

    @Autowired
    TradeService tradeService;

    @ApiOperation(value = "Create new trade entry", response = TradeDTO.class)
    @PostMapping(produces = "application/json")
    public TradeDTO createTrade(@RequestBody TradeDTO tradeDTO) {
        try {
            logger.info("Create trade request : {}", tradeDTO);
            TradeDTO response = tradeService.createTradeRecords(tradeDTO);
            logger.info("Create trade response : {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error in creating a new trade entry: {}", ExceptionUtils.getStackTrace(e));
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

    @ApiOperation(value = "Retrieve all trades", response = FetchTradeResponse.class)
    @GetMapping(produces = "application/json")
    public FetchTradeResponse getTrades(@RequestParam(value = "userId") Long userId,
                                        @RequestParam(value = "tradeIds", required = false) List<Long> tradeIds) {
        try {
            logger.info("Retrieve trade request userId: {}, tradeIds: {}", userId, tradeIds);
            FetchTradeResponse response = tradeService.getTradeRecords(userId, tradeIds);
            logger.info("Retrieve trades response : {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error in retrieving trade entries: {}", ExceptionUtils.getStackTrace(e));
            return FetchTradeResponse.builder().message("Error").success(false).build();
        }
    }

    @ApiOperation(value = "Update trade entry", response = TradeDTO.class)
    @PutMapping(produces = "application/json")
    public TradeDTO updateTrade(@RequestBody TradeDTO tradeDTO) {
        try {
            logger.info("Update trade request : {}", tradeDTO);
            TradeDTO response = tradeService.updateTradeRecord(tradeDTO);
            logger.info("Update trade response : {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error in updating a trade entry: {}", ExceptionUtils.getStackTrace(e));
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

    @ApiOperation(value = "Delete a trade entry", response = TradeDTO.class)
    @DeleteMapping(produces = "application/json")
    public TradeDTO deleteTrade(@RequestBody TradeDTO tradeDTO) {
        try {
            logger.info("Delete trade request : {}", tradeDTO);
            TradeDTO response = tradeService.deleteTradeRecord(tradeDTO);
            logger.info("Delete trade response : {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error in deleting a new entry: {}", ExceptionUtils.getStackTrace(e));
            return TradeDTO.builder().message("Error").success(false).build();
        }
    }

}
