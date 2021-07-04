package com.smallcase.services;

import com.smallcase.domainentity.Trade;
import com.smallcase.dto.TradeDTO;
import com.smallcase.repository.TradeRepository;
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
    @Qualifier("TradeDTOValidator")
    DomainValidator<TradeDTO> tradeDTOValidator;

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    UserSecurityService userSecurityService;

}
