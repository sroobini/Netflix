package com.identity.platform.model.api;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class LoginRequest {
    @NotBlank
    private String codeChallenge;

    @NotBlank
    private String codeChallengeMethod;

    private String responseType;

    private String responseMode;

    private String scope;

    @NotBlank
    private String redirectUri;

    public LoginRequest(String codeChallenge, String codeChallengeMethod,
                        String responseType, String responseMode, String redirectUri, String scope) {
        this.codeChallenge = codeChallenge;
        this.codeChallengeMethod = codeChallengeMethod;
        this.responseType = responseType;
        this.responseMode = responseMode;
        this.scope = scope;
        this.redirectUri = redirectUri;

    }

}

