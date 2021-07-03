package com.smallcase.validators;

import com.smallcase.domainentity.Trade;
import com.smallcase.exception.FatalCustomException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("TradeInfoValidator")
public class TradeInfoValidator implements DomainValidator<Trade> {

    @Override
    public boolean validate(Trade trade) throws FatalCustomException {
        return (trade.getQuantity() >= 0) && (trade.getPrice() >= 0.0);
    }
}
