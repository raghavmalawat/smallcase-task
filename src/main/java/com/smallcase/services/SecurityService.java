package com.smallcase.services;

import com.smallcase.domainentity.Security;
import com.smallcase.dto.SecurityDTO;
import com.smallcase.exception.FatalCustomException;
import com.smallcase.exception.FatalErrorCode;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SecurityService {

    @Autowired
    SecurityHelper securityHelper;

    public SecurityDTO addSecurity(SecurityDTO securityDTO) {
        try {
            if (!securityHelper.getSecurityDTOValidator().validate(securityDTO))
                throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_DTO_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_DTO_INVALID.getType());

            if (Objects.isNull(securityDTO.getSecurities()))
                throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_DTO_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_DTO_INVALID.getType());

            for (Security security : securityDTO.getSecurities()) {
                security.addSecurity(securityHelper);
            }

            securityDTO.setSuccess(Boolean.TRUE);
            securityDTO.setMessage("Success");

            securityDTO.setSuccess(Boolean.TRUE);
            securityDTO.setMessage("Success");
            return securityDTO;
        } catch (FatalCustomException e) {
            return SecurityDTO.builder().success(Boolean.FALSE).message(e.getMessage()).build();
        }
    }

    public SecurityDTO getSecurities(List<Long> securityIds) {
        List<Security> securities = new ArrayList<>();

        if (Objects.nonNull(securityIds))
            securities = securityIds.stream().map(Security::new).collect(Collectors.toList());

        List<Security> securityList = new ArrayList<>(securityHelper.getSecurityRepository().bulkGet(null, securities));
        SecurityDTO response = SecurityDTO.builder().securities(new ArrayList<>()).build();

        if (CollectionUtils.isEmpty(securityList)) {
            response.setMessage("No Securities Found");
            response.setSuccess(Boolean.FALSE);
            return response;
        }
        response.setSecurities(securityList);
        response.setMessage("Success");
        response.setSuccess(Boolean.TRUE);
        return response;
    }
}
