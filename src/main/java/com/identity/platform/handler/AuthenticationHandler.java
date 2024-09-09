package com.identity.platform.handler;

import com.identity.platform.cache.AuthCache;
import com.identity.platform.error.*;
import com.identity.platform.exception.*;
import com.identity.platform.model.api.*;
import com.identity.platform.model.common.Actor;
import com.identity.platform.model.common.Subject;
import com.identity.platform.model.common.TokenContext;
import com.identity.platform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.identity.platform.common.CONSTANTS.PKCE_AUTH_MECHANISM;


/**
 * The class AuthenticationHandler has the implementation for the REST interface IAuthentication
 */
@Service
public class AuthenticationHandler {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthCache authCache;

    /**
     *  This method authenticates user and returns a call back URL with authorization code
     * @param headers
     * @param request
     * @return
     */
    public ResponseEntity<?> authenticateLogin(String headers, LoginRequest request) {

        try {
            validateLoginRequest(request);
            Actor actor = authenticationService.authenticateActor(headers);
            TokenContext tokenContext = new TokenContext();
            tokenContext.setActor(actor);
            Subject subject = authenticationService.validateUser(request);
            tokenContext.setSubject(subject);

            tokenContext.setScope(request.getScope());
            tokenContext.setRedirectUri(request.getRedirectUri());
            tokenContext.setResponseType(request.getResponseType());
            tokenContext.setResponseMode(request.getResponseMode());

            // Get the PKCE authentication mechanism based on the request parameters
            String authMechanism = authenticationService.getAuthMechanism(request);

            if(authMechanism.equals(PKCE_AUTH_MECHANISM)) {
                authenticationService.validatePKCEParameters(request, tokenContext);
            }
            tokenContext.setAuthCode(authenticationService.generateCode(tokenContext));

            // set cache
            authCache.put(tokenContext.getNonce(), tokenContext);

            // construct url with code param
            StringBuilder responseUrl = new StringBuilder(request.getRedirectUri()).append("?code=").append(tokenContext.getAuthCode());

            return ResponseEntity.ok(responseUrl.toString());
        } catch (CODE_GENERATION_ERROR | INVALID_CLIENT_EXCEPTION |
                 INVALID_USER_EXCEPTION | INVALID_CODE_VERIFIER_EXCEPTION |
                 INVALID_CODE_CHALLENGE_EXCEPTION error) {
            return new ResponseEntity(new ErrorResponse(error.getMessage(),
                    "Authentication Failure"), HttpStatus.UNAUTHORIZED);
        } catch (CLIENT_CREDENTIALS_LOAD_ERROR | FILE_READING_ERROR |
                 SIGNING_KEY_LOAD_ERROR | USER_LOAD_ERROR error) {
            return new ResponseEntity(new ErrorResponse(error.getMessage(),
                    "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method exchanges an Authorization code to ID Token
     * @param authorizationHeader
     * @param request
     * @return
     */
    public ResponseEntity<IResponse> createToken(String authorizationHeader, TokenRequest request) {

        try {
            validateTokenRequest(request);
            // API caller validation. can be used to allow/deny the code to token exchange
            authenticationService.authenticateActor(authorizationHeader);

            TokenContext tokenContext = tokenService.createToken(request);
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setIdToken(tokenContext.getIdToken());
            return ResponseEntity.ok(tokenResponse);

        } catch (INVALID_REDIRECT_URI_EXCEPTION | INVALID_CODE_EXCEPTION | INVALID_CODE_VERIFIER_EXCEPTION
                exception) {
            return new ResponseEntity(new ErrorResponse(exception.getMessage(),
                    "Authorization Failure"), HttpStatus.UNAUTHORIZED);
        } catch ( CLIENT_CREDENTIALS_LOAD_ERROR| FILE_READING_ERROR | SIGNING_KEY_LOAD_ERROR |
        USER_LOAD_ERROR error) {
            return new ResponseEntity(new ErrorResponse(error.getMessage(),
                    "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * This method validates the loginRequest
     * @param request
     * @throws Exception
     */
    private void validateLoginRequest(LoginRequest request) throws INVALID_REDIRECT_URI_EXCEPTION {
        if(request.getRedirectUri() == null) {
            throw new INVALID_REDIRECT_URI_EXCEPTION("Missing Redirect URI");
        }
    }

    /**
     * This method validates the token request
     * @param request
     * @throws Exception
     */
    private void validateTokenRequest(TokenRequest request) throws INVALID_REDIRECT_URI_EXCEPTION ,
            INVALID_CLIENT_EXCEPTION {
        if(request.getRedirect_uri().isEmpty()) {
            throw new INVALID_REDIRECT_URI_EXCEPTION("Missing Redirect URI");
        }
        if(!request.getGrant_type().equals("authorization_code")) {
            throw new INVALID_CLIENT_EXCEPTION("Invalid Grant Type");

        }
        // add more request validation logic here
    }
}
