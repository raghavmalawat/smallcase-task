package com.smallcase.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.smallcase.domainentity.Security;
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
public class UserSecurityInfo {

    @JsonProperty("user_security_id")
    @SerializedName("user_security_id")
    Long userSecurityId;

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
}
