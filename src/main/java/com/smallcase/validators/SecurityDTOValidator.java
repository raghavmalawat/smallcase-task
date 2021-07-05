package com.smallcase.validators;

import com.smallcase.domainentity.Security;
import com.smallcase.dto.SecurityDTO;
import com.smallcase.exception.FatalCustomException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Qualifier("SecurityDTOValidator")
public class SecurityDTOValidator implements DomainValidator<SecurityDTO> {

    /**
     * @param securityDTO validate if the securities have all the required fields populated
     * @return true if validation successful else false
     */
    @Override
    public boolean validate(SecurityDTO securityDTO) throws FatalCustomException {
        for (Security security : securityDTO.getSecurities()) {
            if (Objects.isNull(security.getSecurityType()) || Objects.isNull(security.getTickerSymbol()) || Objects.isNull(security.getSecurityName()))
                return false;
        }
        return true;
    }
}

