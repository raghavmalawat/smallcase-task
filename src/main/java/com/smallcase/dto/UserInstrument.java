package com.smallcase.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.smallcase.enums.SecurityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInstrument {

    @JsonProperty("security_type")
    @SerializedName("security_type")
    @NotNull
    SecurityType securityType;

    @JsonProperty("cumulative_returns")
    @SerializedName("cumulative_returns")
    Double cumulativeReturns;

    @JsonProperty("securities")
    @SerializedName("securities")
    @NotNull
    List<UserSecurityInfo> userSecurities;
}
