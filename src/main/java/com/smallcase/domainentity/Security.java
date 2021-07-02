package com.smallcase.domainentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.smallcase.enums.SecurityType;
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
    @NotNull
    Long securityId;

    @JsonProperty("security_type")
    @SerializedName("security_type")
    @NotNull
    SecurityType securityType;

}
