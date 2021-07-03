package com.smallcase.services;

import com.smallcase.domainentity.Trade;
import com.smallcase.utils.DateTimeUtils;
import com.smallcase.validators.DomainValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Getter
public class TradeHelper {

    @Autowired
    @Qualifier("TradeInfoValidator")
    DomainValidator<Trade> tradeInfoValidator;

    @Autowired
    DateTimeUtils dateTimeUtils;

}
