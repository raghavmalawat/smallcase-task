package com.smallcase.services;

import com.smallcase.domainentity.Trade;
import com.smallcase.domainentity.UserSecurity;
import com.smallcase.dto.UserSecurityDTO;
import com.smallcase.enums.Status;
import com.smallcase.enums.TradeType;
import com.smallcase.exception.FatalCustomException;
import com.smallcase.exception.FatalErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserSecurityService {

    @Autowired
    UserSecurityHelper userSecurityHelper;

    public Boolean upsertUserSecurity(Trade trade) throws FatalCustomException {
        UserSecurity userSecurityFromDB = userSecurityHelper.getUserSecurityRepository().get(new UserSecurity(trade));
        UserSecurity updatedObj = calculateLatestAverageAndQuantity(userSecurityFromDB, trade);

        try {
            if (Objects.nonNull(userSecurityFromDB))
                updatedObj.updateUserSecurity(userSecurityHelper, userSecurityFromDB);
            else
                updatedObj.addUserSecurity(userSecurityHelper);

            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    private UserSecurity calculateLatestAverageAndQuantity(UserSecurity userSecurityFromDB, Trade trade) throws FatalCustomException {
        Long currentQuantity = 0L;
        Double averagePrice;
        Double totalPrice = 0.0;

        if (Objects.nonNull(userSecurityFromDB) && userSecurityFromDB.getStatus().equals(Status.ACTIVE)) {
            currentQuantity = userSecurityFromDB.getCurrentQuantity();
            averagePrice = userSecurityFromDB.getAveragePrice();
            totalPrice = currentQuantity * averagePrice;
        }

        if (trade.getTradeType().equals(TradeType.BUY)) {
            totalPrice = totalPrice + (trade.getPrice() * trade.getQuantity());
            currentQuantity = currentQuantity + trade.getQuantity();
        } else {
            if (trade.getQuantity() > currentQuantity)
                    throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_QUANTITY_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_QUANTITY_INVALID.getType());
            totalPrice = totalPrice - (trade.getPrice() * trade.getQuantity());
            currentQuantity = currentQuantity - trade.getQuantity();
        }

        averagePrice = totalPrice / currentQuantity;

        if (Objects.isNull(userSecurityFromDB))
            return new UserSecurity(currentQuantity, averagePrice, trade.getUserId(), trade.getSecurity());

        return new UserSecurity(currentQuantity, averagePrice, userSecurityFromDB);
    }


    public void addUserSecurity() {

    }
}
