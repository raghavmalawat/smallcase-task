package com.smallcase.services;

import com.smallcase.domainentity.Trade;
import com.smallcase.domainentity.UserSecurity;
import com.smallcase.dto.UserInstrument;
import com.smallcase.dto.UserSecurityDTO;
import com.smallcase.enums.Status;
import com.smallcase.enums.TradeAction;
import com.smallcase.enums.TradeType;
import com.smallcase.exception.FatalCustomException;
import com.smallcase.exception.FatalErrorCode;
import com.smallcase.transformer.UserSecurityToUserSecurityDTOTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserSecurityService {

    @Autowired
    UserSecurityToUserSecurityDTOTransformer userSecurityToUserSecurityDTOTransformer;

    @Autowired
    UserSecurityHelper userSecurityHelper;

    public Boolean upsertUserSecurity(Trade trade) throws FatalCustomException {
        UserSecurity userSecurityFromDB = userSecurityHelper.getUserSecurityRepository().get(new UserSecurity(trade));
        UserSecurity updatedObj = calculateLatestAverageAndQuantity(userSecurityFromDB, trade, trade.getTradeType(), TradeAction.NORMAL);
        // Avg Buy Price will update if BUY
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

    public Boolean updateUserSecurity(Trade trade, Trade earlierTrade) throws FatalCustomException {
        UserSecurity userSecurityFromDB = userSecurityHelper.getUserSecurityRepository().get(new UserSecurity(trade));
        UserSecurity updatedObj = null;

        // revert Back Trade
        if (earlierTrade.getTradeType().equals(TradeType.BUY)) // this will update the avg buy price
            updatedObj = calculateLatestAverageAndQuantity(userSecurityFromDB, earlierTrade, TradeType.SELL, TradeAction.REVERT);
        else if (earlierTrade.getTradeType().equals(TradeType.SELL)) // this will just add the quantity
            updatedObj = calculateLatestAverageAndQuantity(userSecurityFromDB, earlierTrade, TradeType.BUY, TradeAction.REVERT);

        // Update new Trade
        updatedObj = calculateLatestAverageAndQuantity(updatedObj, trade, trade.getTradeType(), TradeAction.NORMAL);


        if (updatedObj.getCurrentQuantity() < 0)
            throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_REVERT_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_REVERT_INVALID.getType());

        try {
            if (Objects.nonNull(updatedObj)) {
                updatedObj.updateUserSecurity(userSecurityHelper, userSecurityFromDB);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    public Boolean deleteTradeSecurity(Trade trade) throws FatalCustomException {
        UserSecurity userSecurityFromDB = userSecurityHelper.getUserSecurityRepository().get(new UserSecurity(trade));
        UserSecurity updatedObj = null;

        if (trade.getTradeType().equals(TradeType.BUY))
            updatedObj = calculateLatestAverageAndQuantity(userSecurityFromDB, trade, TradeType.SELL, TradeAction.REVERT);
        else if (trade.getTradeType().equals(TradeType.SELL))
            updatedObj = calculateLatestAverageAndQuantity(userSecurityFromDB, trade, TradeType.BUY, TradeAction.REVERT);

        try {
            if (Objects.nonNull(updatedObj)) {

                if (updatedObj.getCurrentQuantity() < 0)
                    throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_REVERT_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_REVERT_INVALID.getType());

                updatedObj.updateUserSecurity(userSecurityHelper, userSecurityFromDB);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    private UserSecurity calculateLatestAverageAndQuantity(UserSecurity userSecurityFromDB, Trade trade, TradeType tradeType, TradeAction tradeAction) throws FatalCustomException {
        Long currentQuantity = 0L;
        Double averagePrice = 0.0;
        Double totalPrice = 0.0;

        if (Objects.nonNull(userSecurityFromDB)) {
            currentQuantity = userSecurityFromDB.getCurrentQuantity();
            averagePrice = userSecurityFromDB.getAveragePrice();
            totalPrice = currentQuantity * averagePrice;
        }

        if (tradeType.equals(TradeType.BUY)) {
            if (tradeAction.equals(TradeAction.REVERT)) {
                currentQuantity = currentQuantity + trade.getQuantity();
            } else {
                totalPrice = totalPrice + (trade.getPrice() * trade.getQuantity());
                currentQuantity = currentQuantity + trade.getQuantity();
                averagePrice = totalPrice / currentQuantity;
            }
        } else {
            if (tradeAction.equals(TradeAction.REVERT)) {
                currentQuantity = currentQuantity - trade.getQuantity();

//                if (currentQuantity < 0)
//                    throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_REVERT_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_REVERT_INVALID.getType());

                totalPrice = totalPrice - (trade.getPrice() * trade.getQuantity());

                if (currentQuantity > 0)
                    averagePrice = totalPrice / currentQuantity;
                else {
                    averagePrice = 0.0;
                    currentQuantity = 0L;
                }
            } else {
                if (trade.getQuantity() > currentQuantity)
                    throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_QUANTITY_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_QUANTITY_INVALID.getType());
                currentQuantity = currentQuantity - trade.getQuantity();
            }
        }

        if (Objects.isNull(userSecurityFromDB) && (currentQuantity > 0))
            return new UserSecurity(currentQuantity, averagePrice, trade.getUserId(), trade.getSecurity());

        if (currentQuantity > 0)
            return new UserSecurity(currentQuantity, averagePrice, Status.ACTIVE, userSecurityFromDB);
        else
            return new UserSecurity(currentQuantity, averagePrice, Status.INACTIVE, userSecurityFromDB);
    }


    public UserSecurityDTO getUserSecurities(Long userId) {
        List<UserSecurity> userSecurityList = new ArrayList<>(userSecurityHelper.getUserSecurityRepository().bulkGet(userId, new ArrayList<>()));
        UserSecurityDTO response = UserSecurityDTO.builder().userId(userId).instruments(new ArrayList<>()).build();

        if (CollectionUtils.isEmpty(userSecurityList)) {
            response.setMessage("No Securities Found");
            response.setSuccess(Boolean.FALSE);
            return response;
        }
        response = userSecurityToUserSecurityDTOTransformer.transformObject(response, userSecurityList);
        response.setMessage("Success");
        response.setSuccess(Boolean.TRUE);
        return response;
    }

    public UserSecurityDTO getPortfolioReturns(Long userId) {
        List<UserSecurity> userSecurityList = new ArrayList<>(userSecurityHelper.getUserSecurityRepository().bulkGet(userId, new ArrayList<>()));
        UserSecurityDTO response = UserSecurityDTO.builder().userId(userId).instruments(new ArrayList<>()).build();

        if (CollectionUtils.isEmpty(userSecurityList)) {
            response.setMessage("No Securities Found, Returns cannot be generated");
            response.setSuccess(Boolean.FALSE);
            return response;
        }

        userSecurityList.forEach(UserSecurity::getSecurityReturns);
        response = userSecurityToUserSecurityDTOTransformer.transformObject(response, userSecurityList);

        response.setTotalReturns(calculateCumulativeReturns(response.getInstruments()));

        response.setMessage("Success");
        response.setSuccess(Boolean.TRUE);
        return response;
    }

    private Double calculateCumulativeReturns(List<UserInstrument> instruments) {
        AtomicReference<Double> cumulativeReturns = new AtomicReference<>(0.0);

        instruments.forEach(userInstrument -> {
            AtomicReference<Double> cumulativeReturnsByType = new AtomicReference<>(0.0);

            userInstrument.getUserSecurities().forEach(userSecurityInfo -> cumulativeReturnsByType.updateAndGet(v -> v + userSecurityInfo.getCumulativeReturns()));

            cumulativeReturns.set(cumulativeReturns.get() + cumulativeReturnsByType.get());
            userInstrument.setCumulativeReturns(cumulativeReturnsByType.get());
        });
        return cumulativeReturns.get();
    }
}
