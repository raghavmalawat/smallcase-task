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

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecurityDTO {

    @JsonProperty("success")
    @SerializedName("success")
    private boolean success;

    @JsonProperty("message")
    @SerializedName("message")
    private String message;

    @JsonProperty("securities")
    @SerializedName("securities")
    List<Security> securities;

}
