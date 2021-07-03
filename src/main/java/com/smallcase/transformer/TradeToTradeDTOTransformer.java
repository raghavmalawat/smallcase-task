package com.smallcase.transformer;

import com.smallcase.domainentity.Trade;
import com.smallcase.dto.TradeDTO;
import com.smallcase.dto.TradeInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeToTradeDTOTransformer implements DomainTransformer<List<Trade>, TradeDTO> {
    @Override
    public TradeDTO transformObject(List<Trade> trades) {
        return null;
    }

    @Override
    public TradeDTO transformObject(TradeDTO tradeDTO, List<Trade> trades) {
       tradeDTO.setTrades(convertTradeToTradeInfo(trades));
       return tradeDTO;
    }

    private List<TradeInfo> convertTradeToTradeInfo(List<Trade> trades) {
        if (CollectionUtils.isNotEmpty(trades)) {
            return trades.stream().map(u ->
                    TradeInfo.builder()
                            .tradeId(u.getTradeId())
                            .tradeType(u.getTradeType())
                            .quantity(u.getQuantity())
                            .price(u.getPrice())
                            .build()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
