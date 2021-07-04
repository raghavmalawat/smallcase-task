package com.smallcase.transformer;

import com.smallcase.domainentity.UserSecurity;
import com.smallcase.dto.UserInstrument;
import com.smallcase.dto.UserSecurityDTO;
import com.smallcase.dto.UserSecurityInfo;
import com.smallcase.enums.SecurityType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserSecurityToUserSecurityDTOTransformer implements DomainTransformer<List<UserSecurity>, UserSecurityDTO> {
    @Override
    public UserSecurityDTO transformObject(List<UserSecurity> userSecurityList) {
        return null;
    }

    @Override
    public UserSecurityDTO transformObject(UserSecurityDTO userSecurityDTO, List<UserSecurity> userSecurities) {
        List<UserInstrument> userInstruments = new ArrayList<>();

        for (SecurityType securityType: SecurityType.values()) {
            List<UserSecurity> filteredSecuritiesList = userSecurities.stream().filter(u -> u.getSecurity().getSecurityType().equals(securityType)).collect(Collectors.toList());
            userInstruments.add(UserInstrument.builder().securityType(securityType).userSecurities(convertUserSecurityToUserSecurityInfo(filteredSecuritiesList)).build());
        }
        return UserSecurityDTO.builder()
                .instruments(userInstruments)
                .userId(userSecurityDTO.getUserId())
                .build();
    }

    private List<UserSecurityInfo> convertUserSecurityToUserSecurityInfo(List<UserSecurity> filteredSecuritiesList) {
        return filteredSecuritiesList.stream().map(userSecurity -> {
            UserSecurityInfo userSecurityInfo = UserSecurityInfo.builder()
                    .averagePrice(userSecurity.getAveragePrice())
                    .currentQuantity(userSecurity.getCurrentQuantity())
                    .security(userSecurity.getSecurity())
                    .build();

            if (Objects.nonNull(userSecurity.getCumulativeReturns()))
                userSecurityInfo.setCumulativeReturns(userSecurity.getCumulativeReturns());

            return userSecurityInfo;
        }
        ).collect(Collectors.toList());
    }
}
