package com.smallcase.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.smallcase.enums.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TradeInfo {

    @JsonProperty("trade_id")
    @SerializedName("trade_id")
    Long tradeId;

    @JsonProperty("trade_type")
    @SerializedName("trade_type")
    @NotNull
    TradeType tradeType;

    @JsonProperty("quantity")
    @SerializedName("quantity")
    @NotNull
    Long quantity;

    @JsonProperty("price")
    @SerializedName("price")
    @NotNull
    Double price;

}
