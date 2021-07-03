package com.smallcase.services;

import com.smallcase.domainentity.Trade;
import com.smallcase.dto.TradeDTO;
import com.smallcase.exception.FatalCustomException;
import com.smallcase.exception.FatalErrorCode;
import com.smallcase.transformer.TradeDTOToTradeTransformer;
import com.smallcase.transformer.TradeToTradeDTOTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    @Autowired
    TradeDTOToTradeTransformer tradeDTOToTradeTransformer;

    @Autowired
    TradeToTradeDTOTransformer tradeToTradeDTOTransformer;

    @Autowired
    TradeHelper tradeHelper;

    public TradeDTO createTradeRecords(TradeDTO tradeDTO) {
        try {
            if (!tradeHelper.getTradeDTOValidator().validate(tradeDTO))
                throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_DTO_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_DTO_INVALID.getType());

            if (CollectionUtils.isNotEmpty(tradeDTO.getTrades())) {
                List<Trade> tradeList = tradeDTOToTradeTransformer.transformObject(tradeDTO);

                if (CollectionUtils.isNotEmpty(tradeList)) {
                    for (Trade trade : tradeList)
                        trade.addTrade(tradeHelper);
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

    public TradeDTO getTradeRecords() {
        return null;
    }

    public TradeDTO updateTradeRecord(TradeDTO tradeDTO) {
        return null;
    }

    public TradeDTO deleteTradeRecord(TradeDTO tradeDTO) {
        return null;
    }
}
