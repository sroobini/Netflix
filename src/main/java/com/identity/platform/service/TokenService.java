package com.identity.platform.service;

import com.identity.platform.cache.AuthCache;
import com.identity.platform.exception.INVALID_CODE_EXCEPTION;
import com.identity.platform.exception.INVALID_CODE_VERIFIER_EXCEPTION;
import com.identity.platform.exception.INVALID_REDIRECT_URI_EXCEPTION;
import com.identity.platform.model.common.TokenContext;
import com.identity.platform.model.api.TokenRequest;
import com.identity.platform.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.identity.platform.common.CONSTANTS.SHA_ALGORITHM_NAME;

@Component
public class TokenService { ;

    @Autowired
    AuthCache authCache;

    @Autowired
    JwtUtils jwtUtils;

    public TokenContext createToken(TokenRequest request) throws INVALID_CODE_EXCEPTION,
            INVALID_CODE_VERIFIER_EXCEPTION {
        TokenContext tokenContext = getTokenContextFromCode(request.getCode());
        if (tokenContext == null) {
            throw new INVALID_CODE_EXCEPTION("Invalid code");
        }
        boolean verified = validateCodeVerifier(request, tokenContext);
        if(!verified) {
            throw new INVALID_CODE_VERIFIER_EXCEPTION("Invalid code verifier");
        }
        validateRedirectUri(request, tokenContext);

        String idToken = jwtUtils.generateJwtToken(tokenContext);
        tokenContext.setIdToken(idToken);
        return tokenContext;

    }

    public TokenContext getTokenContextFromCode(String code) {
        String encodedCode = code.startsWith("C21") ? code.substring(3) : code;
        TokenContext tokenContext = (TokenContext) authCache.get(encodedCode);
        // since code is one time use, remove from cache
        authCache.remove(encodedCode);

        return tokenContext;
    }

    public boolean validateCodeVerifier(TokenRequest request, TokenContext tokenContext) {
        boolean verified = false;
        String codeChallengeMethod = tokenContext.getCodeChallengeMethod();
        String codeChallenge = tokenContext.getCodeChallenge();
        if(codeChallengeMethod != null && codeChallenge != null) {
            String codeVerifier = request.getCode_verifier();
            if(codeChallengeMethod.equals(SHA_ALGORITHM_NAME)) {

                byte[] verifierS256 = getSHA256HashBytes(codeVerifier);
                String encodedCodeVerifier = Base64.getUrlEncoder().withoutPadding().encodeToString(verifierS256);
                if(encodedCodeVerifier.equals(codeChallenge)) {
                    verified = true;
                    tokenContext.setProofKeyAuthentication(verified);
                }
            }
        }
        return verified;
    }

    private void validateRedirectUri(TokenRequest request, TokenContext tokenContext)
            throws INVALID_REDIRECT_URI_EXCEPTION {
        if(!request.getRedirect_uri().equals(tokenContext.getRedirectUri())) {
            throw new INVALID_REDIRECT_URI_EXCEPTION("Redirect uri mismatch");
        }
    }

    private byte[] getSHA256HashBytes(String input) throws INVALID_CODE_VERIFIER_EXCEPTION {
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        if(inputBytes == null) {
            throw new INVALID_CODE_VERIFIER_EXCEPTION("null_value_for_hash");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(inputBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new INVALID_CODE_VERIFIER_EXCEPTION("Invalid Code verifier");
        }
    }
}
