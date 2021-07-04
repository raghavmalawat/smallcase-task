package com.smallcase.domainentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.smallcase.database.postgres.entity.TradeEntity;
import com.smallcase.dto.TradeInfo;
import com.smallcase.enums.SecurityType;
import com.smallcase.enums.TradeType;
import com.smallcase.exception.FatalCustomException;
import com.smallcase.exception.FatalErrorCode;
import com.smallcase.services.TradeHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Trade {

    @JsonProperty("trade_id")
    @SerializedName("trade_id")
    Long tradeId;

    @JsonProperty("trade_type")
    @SerializedName("trade_type")
    @NotNull
    TradeType tradeType;

    @JsonProperty("user_id")
    @SerializedName("user_id")
    @NotNull
    Long userId;

    @JsonProperty("security")
    @SerializedName("security")
    @NotNull
    Security security;

    @JsonProperty("quantity")
    @SerializedName("quantity")
    @NotNull
    Long quantity;

    @JsonProperty("price")
    @SerializedName("price")
    @NotNull
    Double price;

    @JsonIgnore
    Integer deletedAt;

    @JsonIgnore
    Integer createdAt;

    @JsonIgnore
    Integer updatedAt;

    public Trade(TradeInfo trade, Long userId, Security security) {
        this.security = security;
        this.userId = userId;
        this.tradeId = trade.getTradeId();
        this.tradeType = trade.getTradeType();
        this.quantity = trade.getQuantity();
        this.price = trade.getPrice();
    }

    public Trade(TradeEntity tradeEntity) {
        Security security1 = new Security(tradeEntity.getSecurityId(), SecurityType.getEnum(tradeEntity.getSecurityType()));

        this.tradeId = tradeEntity.getId();
        this.tradeType = TradeType.getEnum(tradeEntity.getTradeType());
        this.userId = tradeEntity.getUserId();
        this.security = security1;
        this.quantity = tradeEntity.getQuantity();
        this.price = tradeEntity.getPrice();
        this.createdAt = tradeEntity.getCreatedAt();
        this.deletedAt = tradeEntity.getDeletedAt();
        this.updatedAt = tradeEntity.getUpdatedAt();
    }

    public void addTrade(TradeHelper tradeHelper) throws FatalCustomException {
        if (!tradeHelper.getTradeInfoValidator().validate(this))
            throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_INFO_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_INFO_INVALID.getType());

        Boolean tradeExecutionPossible = tradeHelper.getUserSecurityService().upsertUserSecurity(this);

        if (tradeExecutionPossible.equals(Boolean.FALSE))
            throw new FatalCustomException(FatalErrorCode.ERR0R_TRADE_EXECUTION_INVALID.getCustomMessage(), FatalErrorCode.ERR0R_TRADE_EXECUTION_INVALID.getType());

        Integer currentTime = tradeHelper.getDateTimeUtils().getIntCurrentTimeInSeconds();
        
        this.createdAt = currentTime;
        this.updatedAt = currentTime;
        this.deletedAt = 0;
        this.tradeId = (Long) tradeHelper.getTradeRepository().add(this);
    }

    public void updateTrade(TradeHelper tradeHelper) throws FatalCustomException {
        if (Objects.isNull(this.tradeId) || !tradeHelper.getTradeInfoValidator().validate(this))
            throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_INFO_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_INFO_INVALID.getType());

        Trade tradeFromDB = tradeHelper.getTradeRepository().get(this);

        if (Objects.isNull(tradeFromDB))
            throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_ID_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_ID_INVALID.getType());

        Boolean tradeExecutionPossible = tradeHelper.getUserSecurityService().updateUserSecurity(this, tradeFromDB);

        if (tradeExecutionPossible.equals(Boolean.FALSE))
            throw new FatalCustomException(FatalErrorCode.ERR0R_TRADE_EXECUTION_INVALID.getCustomMessage(), FatalErrorCode.ERR0R_TRADE_EXECUTION_INVALID.getType());

        this.updatedAt = tradeHelper.getDateTimeUtils().getIntCurrentTimeInSeconds();
        this.createdAt = tradeFromDB.getCreatedAt();
        this.deletedAt = 0;
        this.security = tradeFromDB.getSecurity();

        tradeHelper.getTradeRepository().update(this);
    }

    public void deleteTrade(TradeHelper tradeHelper) throws FatalCustomException {
        if (Objects.isNull(this.tradeId))
            throw new FatalCustomException(FatalErrorCode.ERROR_TRADE_INFO_INVALID.getCustomMessage(), FatalErrorCode.ERROR_TRADE_INFO_INVALID.getType());

        Trade tradeFromDB = tradeHelper.getTradeRepository().get(this);

        // Do computations
        Integer currentTime = tradeHelper.getDateTimeUtils().getIntCurrentTimeInSeconds();

        this.updatedAt = currentTime;
        this.deletedAt = currentTime;
        this.createdAt = tradeFromDB.getCreatedAt();
        this.security = tradeFromDB.getSecurity();

        tradeHelper.getTradeRepository().update(this);
    }
}
