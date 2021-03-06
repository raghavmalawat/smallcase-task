package com.smallcase.validators;

import com.smallcase.dto.TradeDTO;
import com.smallcase.exception.FatalCustomException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Qualifier("TradeDTOValidator")
public class TradeDTOValidator implements DomainValidator<TradeDTO> {

    /**
     * @param tradeDTO validates if any trade has all the required fields set
     * @return true if validation successful else false
     */
    @Override
    public boolean validate(TradeDTO tradeDTO) throws FatalCustomException {
        return (Objects.nonNull(tradeDTO.getUserId())
                && Objects.nonNull(tradeDTO.getSecurity())
                && Objects.nonNull(tradeDTO.getSecurity().getSecurityId())
                && Objects.nonNull(tradeDTO.getSecurity().getSecurityType())
        );
    }
}
