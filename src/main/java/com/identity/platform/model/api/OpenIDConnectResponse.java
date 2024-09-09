package com.identity.platform.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenIDConnectResponse implements IResponse {

    @JsonProperty("issuer")
    String issuer;

    @JsonProperty("authorization_endpoint")
    String authorization_endpoint;

    @JsonProperty("token_endpoint")
    String token_endpoint;

    @JsonProperty("jwks_uri")
    String jwks_uri;

}
