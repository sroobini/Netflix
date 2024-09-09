package com.identity.platform.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenContext {
    Actor actor;
    Subject subject;
    String scope;
    String nonce;
    String codeChallenge;
    String codeChallengeMethod;
    String authCode;
    long creationTime;
    int expiryTime;
    String gsid;
    String redirectUri;
    String responseType;
    String responseMode;
    boolean proofKeyAuthentication;
    String idToken;

}
