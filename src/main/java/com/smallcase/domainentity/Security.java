package com.smallcase.domainentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.smallcase.database.postgres.entity.SecurityEntity;
import com.smallcase.enums.SecurityType;
import com.smallcase.helpers.SecurityHelper;
import lombok.*;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@ToString
public class Security {

    @JsonProperty("security_id")
    @SerializedName("security_id")
    Long securityId;

    @JsonProperty("security_type")
    @SerializedName("security_type")
    @NotNull
    SecurityType securityType;

    @JsonProperty("name")
    @SerializedName("name")
    @NotNull
    String securityName;

    @JsonProperty("ticker_symbol")
    @SerializedName("ticker_symbol")
    @NotNull
    String tickerSymbol;

    @JsonIgnore
    Integer deletedAt;

    @JsonIgnore
    Integer createdAt;

    @JsonIgnore
    Integer updatedAt;

    public Security(Long securityId, SecurityType securityType) {
        this.securityId = securityId;
        this.securityType = securityType;
    }

    public Security(Long securityId) {
        this.securityId = securityId;
    }

    public Security(SecurityEntity securityEntity) {
        this.securityId = securityEntity.getId();
        this.securityType = SecurityType.getEnum(securityEntity.getSecurityType());
        this.securityName = securityEntity.getName();
        this.tickerSymbol = securityEntity.getTickerSymbol();
        this.createdAt = securityEntity.getCreatedAt();
        this.deletedAt = securityEntity.getDeletedAt();
        this.updatedAt = securityEntity.getUpdatedAt();
    }

    public void addSecurity(SecurityHelper securityHelper) {
        Integer currentTime = securityHelper.getDateTimeUtils().getIntCurrentTimeInSeconds();

        this.createdAt = currentTime;
        this.updatedAt = currentTime;
        this.deletedAt = 0;
        this.securityId = (Long) securityHelper.getSecurityRepository().add(this);
    }
}
