package com.smallcase.domainentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.smallcase.enums.TradeType;
import com.smallcase.exception.FatalCustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

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

    public void addTrade() throws FatalCustomException {

    }
}
