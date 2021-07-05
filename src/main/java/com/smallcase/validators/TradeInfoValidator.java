package com.smallcase.validators;

import com.smallcase.domainentity.Trade;
import com.smallcase.exception.FatalCustomException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("TradeInfoValidator")
public class TradeInfoValidator implements DomainValidator<Trade> {

    /**
     * @param trade should have positive quantity and positive trading price
     * @return true if validation successful else false
     */
    @Override
    public boolean validate(Trade trade) throws FatalCustomException {
        return (trade.getQuantity() > 0) && (trade.getPrice() >= 0.0);
    }
}
