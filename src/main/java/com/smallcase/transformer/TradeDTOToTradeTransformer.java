package com.smallcase.transformer;

import com.smallcase.domainentity.Trade;
import com.smallcase.dto.TradeDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeDTOToTradeTransformer implements DomainTransformer<TradeDTO, List<Trade>>  {
    @Override
    public List<Trade> transformObject(TradeDTO tradeDTO) {
        return tradeDTO.getTrades()
                .stream()
                .map(trade -> new Trade(trade, tradeDTO.getUserId(), tradeDTO.getSecurity()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Trade> transformObject(List<Trade> trades, TradeDTO tradeDTO) {
        return null;
    }

}
