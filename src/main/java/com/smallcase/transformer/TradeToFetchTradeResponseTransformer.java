package com.smallcase.transformer;

import com.smallcase.domainentity.Trade;
import com.smallcase.dto.FetchTradeResponse;
import com.smallcase.dto.TradeSecurity;
import com.smallcase.enums.SecurityType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeToFetchTradeResponseTransformer implements DomainTransformer<List<Trade>, FetchTradeResponse>  {

    @Override
    public FetchTradeResponse transformObject(List<Trade> trades) {
        return null;
    }

    @Override
    public FetchTradeResponse transformObject(FetchTradeResponse fetchTradeResponse, List<Trade> trades) {
        List<TradeSecurity> tradeSecurities = new ArrayList<>();

        for (SecurityType securityType: SecurityType.values()) {
            List<Trade> filteredTradeList = trades.stream().filter(u -> u.getSecurity().getSecurityType().equals(securityType)).collect(Collectors.toList());
            tradeSecurities.add(TradeSecurity.builder().securityType(securityType).trades(filteredTradeList).build());
        }

        fetchTradeResponse.setInstruments(tradeSecurities);
        return fetchTradeResponse;
    }
}
