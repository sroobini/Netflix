package com.identity.platform.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse implements IResponse {
    @JsonProperty("id_token")
    String idToken;
}
