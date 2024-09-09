package com.identity.platform.model.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TokenRequest {
    @NotBlank
    private String code;
    private String grant_type;
    @NotBlank
    private String code_verifier;
    private String redirect_uri;

    public TokenRequest(String code, String grant_type, String code_verifier, String redirect_uri) {
        this.code = code;
        this.grant_type = grant_type;
        this.code_verifier = code_verifier;
        this.redirect_uri = redirect_uri;
    }
}
