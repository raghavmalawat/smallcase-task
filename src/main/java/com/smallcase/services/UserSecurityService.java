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
import com.smallcase.helpers.SecurityHelper;
import com.smallcase.helpers.UserSecurityHelper;
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

    @Autowired
    SecurityHelper securityHelper;

    /**
     * @param trade will take the decision if the user is buying a security for the first time or already
     *        holds certain quantity of the security
     * @return a boolean value depending on if the upsert operation can be carried out
     * @throws FatalCustomException in cases when there is some calculation mismatch or some validation checks
     */
    public Boolean upsertUserSecurity(Trade trade) throws FatalCustomException {
        // Fetching the current details for the security associated in the trade
        UserSecurity userSecurityFromDB = userSecurityHelper.getUserSecurityRepository().get(new UserSecurity(trade));

        // Calculating new current quantity and average price in case of BUY trade
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

    /**
     * This function is called when a trade is edited
     * @param trade is the updated trade user poses
     * @param earlierTrade is original trade the user placed
     * @return a boolean value depending on if the update trade is possible or not (current quantity cannot be less than 0)
     * @throws FatalCustomException in case of validation exceptions or if values goes beyond the defined bound
     */
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


        // if the user requests for a SELL trade then often the quantity can be less than 0,
        // have to terminate the update in such cases
        if (updatedObj.getCurrentQuantity() < 0)
            throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_REVERT_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_REVERT_INVALID.getType());

        try {
            // updating the average buy price and quantity for a user after the update
            updatedObj.updateUserSecurity(userSecurityHelper, userSecurityFromDB);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    /**
     * This function is responsible for reverting back the change on the user security quantity and average buy price if applicable
     * @param trade is the trade that needs to be deleted
     * @return a boolean value depending on if the update trade is possible or not (current quantity cannot be less than 0)
     * @throws FatalCustomException in case of validation exceptions or if values goes beyond the defined bound
     */
    public Boolean deleteTradeSecurity(Trade trade) throws FatalCustomException {
        UserSecurity userSecurityFromDB = userSecurityHelper.getUserSecurityRepository().get(new UserSecurity(trade));
        UserSecurity updatedObj = null;

        // TradeAction being REVERT since we want to undo the effects of the trade
        if (trade.getTradeType().equals(TradeType.BUY))
            updatedObj = calculateLatestAverageAndQuantity(userSecurityFromDB, trade, TradeType.SELL, TradeAction.REVERT);
        else if (trade.getTradeType().equals(TradeType.SELL))
            updatedObj = calculateLatestAverageAndQuantity(userSecurityFromDB, trade, TradeType.BUY, TradeAction.REVERT);

        try {
            if (Objects.nonNull(updatedObj)) {

                if (updatedObj.getCurrentQuantity() < 0)
                    throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_REVERT_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_REVERT_INVALID.getType());

                // updating user security holdings if all validations pass
                updatedObj.updateUserSecurity(userSecurityHelper, userSecurityFromDB);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    /**
     * @param userSecurityFromDB is the security holding ofr a user if exists else null
     * @param trade is the upcoming trade the user has placed/updated/deleted
     * @param tradeType is responsible for the type of calculation to be made, can take values BUY and SELL
     * @param tradeAction branches what type of calculation has to be carried out, in case of revert (update, delete trade)
     *        calculations for average buy price and quantity has to be tackled in a different manner
     * @return the updated User security holding and average buy price
     * @throws FatalCustomException in case of validation exceptions or if values goes beyond the defined bound
     */
    private UserSecurity calculateLatestAverageAndQuantity(UserSecurity userSecurityFromDB, Trade trade, TradeType tradeType, TradeAction tradeAction) throws FatalCustomException {
        Long currentQuantity = 0L;
        Double averagePrice = 0.0;
        Double totalPrice = 0.0;

        if (Objects.nonNull(userSecurityFromDB)) {
            // update details if some holdings exist for user security
            currentQuantity = userSecurityFromDB.getCurrentQuantity();
            averagePrice = userSecurityFromDB.getAveragePrice();
            totalPrice = currentQuantity * averagePrice;
        }

        if (tradeType.equals(TradeType.BUY)) {
            if (tradeAction.equals(TradeAction.REVERT)) {
                // when original trade was SELL, just add quantity as it didn't affect the BUY Price earlier
                currentQuantity = currentQuantity + trade.getQuantity();
            } else {
                // in normal trade case, BUY will update both the net quantity and average price
                totalPrice = totalPrice + (trade.getPrice() * trade.getQuantity());
                currentQuantity = currentQuantity + trade.getQuantity();
                averagePrice = totalPrice / currentQuantity;
            }
        } else {
            if (tradeAction.equals(TradeAction.REVERT)) {
                // when original trade was BUY, decrease the quantity and update the average buy price as it affected the BUY Price earlier

                currentQuantity = currentQuantity - trade.getQuantity();
                totalPrice = totalPrice - (trade.getPrice() * trade.getQuantity());

                if (currentQuantity > 0)
                    averagePrice = totalPrice / currentQuantity;
                else {
                    averagePrice = 0.0;
                    currentQuantity = 0L;
                }
            } else {
                // in normal trade case, SELL will update just the net quantity

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
        // INACTIVE will keep an entry in DB but not return when the user fetches his/her portfolio
    }

    /**
     * @param userId signifies the user for whom the portfolio has to be fetched
     * @return all instruments the user holds, grouped by security type
     */
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

    /**
     * @param userId signifies the user for whom the cumulative returns has to be calculated
     * @return all instruments the user holds, grouped by security type along with the cumulative and total returns
     */
    public UserSecurityDTO getPortfolioReturns(Long userId) {
        List<UserSecurity> userSecurityList = new ArrayList<>(userSecurityHelper.getUserSecurityRepository().bulkGet(userId, new ArrayList<>()));
        UserSecurityDTO response = UserSecurityDTO.builder().userId(userId).instruments(new ArrayList<>()).build();

        if (CollectionUtils.isEmpty(userSecurityList)) {
            response.setMessage("No Securities Found, Returns cannot be generated");
            response.setSuccess(Boolean.FALSE);
            return response;
        }

        userSecurityList.forEach(userSecurity -> userSecurity.getSecurityReturns(userSecurityHelper));
        response = userSecurityToUserSecurityDTOTransformer.transformObject(response, userSecurityList);

        response.setTotalReturns(calculateCumulativeReturns(response.getInstruments()));

        response.setMessage("Success");
        response.setSuccess(Boolean.TRUE);
        return response;
    }

    /**
     * @param instruments is the list of securities a user holds
     * @return the total and cumulative returns for a portfolio. This function is responsible for the calculation
     * of total returns of all instruments and even by security type
     */
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
