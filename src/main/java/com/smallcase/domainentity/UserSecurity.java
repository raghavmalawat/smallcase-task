package com.smallcase.domainentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.smallcase.database.postgres.entity.UserSecurityEntity;
import com.smallcase.enums.SecurityType;
import com.smallcase.enums.Status;
import com.smallcase.exception.FatalCustomException;
import com.smallcase.exception.FatalErrorCode;
import com.smallcase.services.TradeHelper;
import com.smallcase.services.UserSecurityHelper;
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
public class UserSecurity {

    @JsonProperty("user_security_id")
    @SerializedName("user_security_id")
    Long userSecurityId;

    @JsonProperty("user_security_status")
    @SerializedName("user_security_status")
    @NotNull
    Status status;

    @JsonProperty("user_id")
    @SerializedName("user_id")
    @NotNull
    Long userId;

    @JsonProperty("security")
    @SerializedName("security")
    @NotNull
    Security security;

    @JsonProperty("current_quantity")
    @SerializedName("current_quantity")
    @NotNull
    Long currentQuantity;

    @JsonProperty("avg_price")
    @SerializedName("avg_price")
    @NotNull
    Double averagePrice;

    @JsonIgnore
    Integer createdAt;

    @JsonIgnore
    Integer updatedAt;

    public UserSecurity(Trade trade) {
        this.userId = trade.getUserId();
        this.security = trade.getSecurity();
        this.status = Status.ACTIVE;
    }

    public UserSecurity(UserSecurityEntity userSecurityEntity) {
        Security security1 = new Security(userSecurityEntity.getSecurityId(), SecurityType.getEnum(userSecurityEntity.getSecurityType()));

        this.userId = userSecurityEntity.getUserId();
        this.userSecurityId = userSecurityEntity.getId();
        this.status = Status.getEnum(userSecurityEntity.getStatus());
        this.security = security1;
        this.currentQuantity = userSecurityEntity.getCurrentQuantity();
        this.averagePrice = userSecurityEntity.getAveragePrice();
        this.createdAt = userSecurityEntity.getCreatedAt();
        this.updatedAt = userSecurityEntity.getUpdatedAt();
    }

    public UserSecurity(Long currentQuantity, Double averagePrice, Status status, UserSecurity userSecurityFromDB) {
        this.currentQuantity = currentQuantity;
        this.averagePrice = averagePrice;

        this.userSecurityId = userSecurityFromDB.userSecurityId;
        this.userId = userSecurityFromDB.userId;
        this.status = status;
        this.security = userSecurityFromDB.security;
        this.createdAt = userSecurityFromDB.createdAt;
        this.updatedAt = userSecurityFromDB.updatedAt;
    }

    public UserSecurity(Long currentQuantity, Double averagePrice, Long userId, Security security) {
        this.currentQuantity = currentQuantity;
        this.averagePrice = averagePrice;
        this.userId = userId;
        this.security = security;
        this.status = Status.ACTIVE;
    }

    public void addUserSecurity(UserSecurityHelper userSecurityHelper) throws FatalCustomException {
        Integer currentTime = userSecurityHelper.getDateTimeUtils().getIntCurrentTimeInSeconds();

        this.createdAt = currentTime;
        this.updatedAt = currentTime;
        this.userSecurityId = (Long) userSecurityHelper.getUserSecurityRepository().add(this);
    }

    public void updateUserSecurity(UserSecurityHelper userSecurityHelper, UserSecurity userSecurityFromDB) throws FatalCustomException {
        this.updatedAt = userSecurityHelper.getDateTimeUtils().getIntCurrentTimeInSeconds();
        this.createdAt = userSecurityFromDB.getCreatedAt();
        this.security = userSecurityFromDB.getSecurity();

        userSecurityHelper.getUserSecurityRepository().update(this);
    }
}
