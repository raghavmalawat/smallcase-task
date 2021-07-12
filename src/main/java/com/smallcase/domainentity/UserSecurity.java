package com.smallcase.domainentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.smallcase.LogFactory;
import com.smallcase.database.postgres.entity.UserSecurityEntity;
import com.smallcase.enums.SecurityType;
import com.smallcase.enums.Status;
import com.smallcase.exception.FatalCustomException;
import com.smallcase.helpers.SecurityHelper;
import com.smallcase.helpers.UserSecurityHelper;
import com.smallcase.utils.WebUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    @JsonProperty("cumulative_returns")
    @SerializedName("cumulative_returns")
    @NotNull
    Double cumulativeReturns;

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

    @Autowired
    WebUtil webUtil;

    @Autowired
    SecurityHelper securityHelper;

    private static final Logger logger = LogFactory.getLogger(UserSecurity.class);

    public UserSecurity(Trade trade) {
        this.userId = trade.getUserId();
        this.security = trade.getSecurity();
        this.status = Status.ACTIVE;
    }

    public UserSecurity(UserSecurityEntity userSecurityEntity) {
        // convert the user security object obtained from DB to the domain object

        Security security1 = new Security(userSecurityEntity.getSecurityId(), SecurityType.getEnum(userSecurityEntity.getSecurityType()));

        List<Security> securityFromDB =  securityHelper.getSecurityRepository().bulkGet(null, Collections.singletonList(security1));

        security1 = securityFromDB.get(0);

        this.userId = userSecurityEntity.getUserId();
        this.userSecurityId = userSecurityEntity.getId();
        this.status = Status.getEnum(userSecurityEntity.getStatus());
        this.security = security1;
        this.currentQuantity = userSecurityEntity.getCurrentQuantity();
        this.averagePrice = userSecurityEntity.getAveragePrice();
        this.createdAt = userSecurityEntity.getCreatedAt();
        this.updatedAt = userSecurityEntity.getUpdatedAt();

        logger.info("user security holding object from DB: {}", this);
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

    public void addUserSecurity(UserSecurityHelper userSecurityHelper) {
        Integer currentTime = userSecurityHelper.getDateTimeUtils().getIntCurrentTimeInSeconds();

        this.createdAt = currentTime;
        this.updatedAt = currentTime;
        this.userSecurityId = (Long) userSecurityHelper.getUserSecurityRepository().add(this);

        logger.info("user security with security id {} added to the system with id {}, current quantity : {}, average buy price : {}",
                this.security.securityId, this.userSecurityId, this.currentQuantity, this.averagePrice);
    }

    public void updateUserSecurity(UserSecurityHelper userSecurityHelper, UserSecurity userSecurityFromDB) throws FatalCustomException {
        this.updatedAt = userSecurityHelper.getDateTimeUtils().getIntCurrentTimeInSeconds();
        this.createdAt = userSecurityFromDB.getCreatedAt();
        this.security = userSecurityFromDB.getSecurity();

        // updating the user security holding in case of create, delete, update trades
        userSecurityHelper.getUserSecurityRepository().update(this);

        logger.info("user security with security id {} updated to the system with id {}, current quantity : {}, average buy price : {}",
                this.security.securityId, this.userSecurityId, this.currentQuantity, this.averagePrice);
    }

    public void getSecurityReturns(UserSecurityHelper userSecurityHelper) {
        // calculate the returns for a particular security considering a fixed current price at the moment
        String tickerSymbol = this.security.tickerSymbol;
        Object currentPrice =  webUtil.getCurrentPrice(Collections.singletonList(tickerSymbol));

        // Get current price and calculate the value

        this.cumulativeReturns = (userSecurityHelper.getCurrentPrice() - this.averagePrice) * this.currentQuantity;
    }
}