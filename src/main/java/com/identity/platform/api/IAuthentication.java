package com.identity.platform.api;

import com.identity.platform.handler.AuthenticationHandler;
import com.identity.platform.model.api.IResponse;
import com.identity.platform.model.api.LoginRequest;
import com.identity.platform.model.api.TokenRequest;
import com.identity.platform.model.api.TokenResponse;
import jakarta.ws.rs.QueryParam;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * This API interface
 *  1. authenticates the User during login
 *  2. creates a token
 */
@RestController
@RequestMapping(path = "oauth2/v1")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class IAuthentication {

    @Autowired
    private final AuthenticationHandler authenticationHandler;

    /**
     *  Authenticates user and returns a call back URL with authorization code
     * @param authorizationHeader
     * @param code_challenge
     * @param response_type
     * @param code_challenge_method
     * @param response_mode
     * @param redirect_uri
     * @param scope
     * @return
     */
    @GetMapping(path = "auth")
    public ResponseEntity<?> authenticateLogin(String authorizationHeader,
                                   @QueryParam("code_challenge") String code_challenge,
                                   @QueryParam("response_type") String response_type,
                                   @QueryParam("code_challenge_method") String code_challenge_method,
                                   @QueryParam("response_mode") String response_mode,
                                   @QueryParam("redirect_uri") String redirect_uri,
                                   @QueryParam("scope") String scope) {
        LoginRequest loginRequest = new LoginRequest(code_challenge, code_challenge_method,
                response_type, response_mode, redirect_uri, scope);
        return authenticationHandler.authenticateLogin(authorizationHeader, loginRequest);
    }

    /**
     * This method exchanges an Authorization code to ID Token
     * @param authorizationHeader
     * @param request
     * @return
     */
    @PostMapping(path = "token",  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<IResponse> createToken(String authorizationHeader,
                                                 @ModelAttribute TokenRequest request) {
        return authenticationHandler.createToken(authorizationHeader, request);
    }


}
