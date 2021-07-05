package com.smallcase.services;

import com.smallcase.domainentity.Trade;
import com.smallcase.dto.FetchTradeResponse;
import com.smallcase.dto.TradeDTO;
import com.smallcase.dto.TradeInfo;
import com.smallcase.exception.FatalCustomException;
import com.smallcase.exception.FatalErrorCode;
import com.smallcase.helpers.TradeHelper;
import com.smallcase.transformer.TradeDTOToTradeTransformer;
import com.smallcase.transformer.TradeToFetchTradeResponseTransformer;
import com.smallcase.transformer.TradeToTradeDTOTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TradeService {

    @Autowired
    TradeDTOToTradeTransformer tradeDTOToTradeTransformer;

    @Autowired
    TradeToTradeDTOTransformer tradeToTradeDTOTransformer;

    @Autowired
    TradeToFetchTradeResponseTransformer tradeToFetchTradeResponseTransformer;

    @Autowired
    TradeHelper tradeHelper;

    public TradeDTO createTradeRecords(TradeDTO tradeDTO) {
        try {
            if (!tradeHelper.getTradeDTOValidator().validate(tradeDTO))
                throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_DTO_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_DTO_INVALID.getType());

            if (CollectionUtils.isNotEmpty(tradeDTO.getTrades())) {
                List<Trade> tradeList = tradeDTOToTradeTransformer.transformObject(tradeDTO);

                if (CollectionUtils.isNotEmpty(tradeList)) {
                    for (Trade trade : tradeList) {
                        trade.addTrade(tradeHelper);
                    }
                }
                TradeDTO response = tradeToTradeDTOTransformer.transformObject(tradeDTO, tradeList);
                response.setSuccess(Boolean.TRUE);
                response.setMessage("Success");
                return response;
            } else {
                tradeDTO.setSuccess(Boolean.TRUE);
                tradeDTO.setMessage("Success");
            }
            return tradeDTO;
        } catch (FatalCustomException e) {
            return TradeDTO.builder().success(Boolean.FALSE).message(e.getMessage()).build();
        }
    }

    public FetchTradeResponse getTradeRecords(Long userId, List<Long> tradeIds) {
        List<Trade> tradeList = new ArrayList<>();

        if (Objects.nonNull(tradeIds) && tradeIds.size() == 1) {
            Trade trade = new Trade(TradeInfo.builder().tradeId(tradeIds.get(0)).build(), userId, null);
            trade = tradeHelper.getTradeRepository().get(trade);
            tradeList.add(trade);
        } else {
            List<TradeInfo> tradeInfoList = new ArrayList<>();
            if (Objects.nonNull(tradeIds))
                tradeInfoList = tradeIds.stream().map(u -> TradeInfo.builder().tradeId(u).build()).collect(Collectors.toList());

            List<Trade> trades = tradeDTOToTradeTransformer.transformObject(TradeDTO.builder().userId(userId).trades(tradeInfoList).build());
            tradeList.addAll(tradeHelper.getTradeRepository().bulkGet(userId, trades));
        }

        FetchTradeResponse response = FetchTradeResponse.builder().userId(userId).instruments(new ArrayList<>()).build();

        if (CollectionUtils.isEmpty(tradeList)) {
            response.setMessage("No Trades Found");
            response.setSuccess(Boolean.FALSE);
            return response;
        }
        response = tradeToFetchTradeResponseTransformer.transformObject(response, tradeList);
        response.setMessage("Success");
        response.setSuccess(Boolean.TRUE);
        return response;
    }

    public TradeDTO updateTradeRecord(TradeDTO tradeDTO) {
        try {
            if (!tradeHelper.getTradeDTOValidator().validate(tradeDTO))
                throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_DTO_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_DTO_INVALID.getType());
            if (CollectionUtils.isNotEmpty(tradeDTO.getTrades())) {
                List<Trade> tradeList = tradeDTOToTradeTransformer.transformObject(tradeDTO);

                if (CollectionUtils.isNotEmpty(tradeList)) {
                    for (Trade trade : tradeList)
                        trade.updateTrade(tradeHelper);
                }
                TradeDTO response = tradeToTradeDTOTransformer.transformObject(tradeList);
                response.setSuccess(Boolean.TRUE);
                response.setMessage("Success");
                return response;
            } else {
                tradeDTO.setSuccess(Boolean.TRUE);
                tradeDTO.setMessage("Success");
            }
            return tradeDTO;
        } catch (FatalCustomException e) {
            return TradeDTO.builder().success(Boolean.FALSE).message(e.getMessage()).build();
        }
    }

    public TradeDTO deleteTradeRecord(TradeDTO tradeDTO) {
        try {
           if (CollectionUtils.isNotEmpty(tradeDTO.getTrades())) {
                List<Trade> tradeList = tradeDTOToTradeTransformer.transformObject(tradeDTO);

                if (CollectionUtils.isNotEmpty(tradeList)) {
                    for (Trade trade : tradeList)
                        trade.deleteTrade(tradeHelper);
                }
                TradeDTO response = tradeToTradeDTOTransformer.transformObject(tradeList);
                response.setSuccess(Boolean.TRUE);
                response.setMessage("Success");
                return response;
            } else {
                tradeDTO.setSuccess(Boolean.TRUE);
                tradeDTO.setMessage("Success");
            }
            return tradeDTO;
        } catch (FatalCustomException e) {
            return TradeDTO.builder().success(Boolean.FALSE).message(e.getMessage()).build();
        }
    }
}
