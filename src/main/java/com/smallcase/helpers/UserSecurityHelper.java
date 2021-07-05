package com.smallcase.helpers;

import com.smallcase.repository.UserSecurityRepository;
import com.smallcase.utils.DateTimeUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class UserSecurityHelper {

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Autowired
    UserSecurityRepository userSecurityRepository;

    @Value("${CURRENT_PRICE_SECURITIES}")
    private Double currentPrice;

}
