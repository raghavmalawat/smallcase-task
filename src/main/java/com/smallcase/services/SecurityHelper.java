package com.smallcase.services;

import com.smallcase.dto.SecurityDTO;
import com.smallcase.repository.SecurityRepository;
import com.smallcase.utils.DateTimeUtils;
import com.smallcase.validators.DomainValidator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Getter
public class SecurityHelper {

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Autowired
    SecurityRepository securityRepository;

    @Autowired
    @Qualifier("SecurityDTOValidator")
    DomainValidator<SecurityDTO> securityDTOValidator;

}
