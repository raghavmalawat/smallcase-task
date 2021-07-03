package com.smallcase.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
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
public class FetchTradeResponse {

    @JsonProperty("success")
    @SerializedName("success")
    private boolean success;

    @JsonProperty("message")
    @SerializedName("message")
    private String message;

    @JsonProperty("user_id")
    @SerializedName("user_id")
    @NotNull
    Long userId;

    @JsonProperty("instruments")
    @SerializedName("instruments")
    @NotNull
    List<TradeSecurity> instruments;

}
