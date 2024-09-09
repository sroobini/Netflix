package com.identity.platform.api;

import jakarta.ws.rs.Produces;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This service interface is to expose  OAuth2/OpenIdConnect Metadata
 * during openidconnect discovery
 */
@RestController
@RequestMapping(path = "/")
@CrossOrigin(origins = "http://localhost:3000")
public interface IMetadata {

    /**
     * Method to get JSON Web Key Config
     * @return
     */
    @GetMapping("oauth2/v1/certs")
    @Produces("application/json")
    ResponseEntity<?> getJwksConfig();

    /**
     *  Method to get the Open ID configuration
     * @return
     */
    @GetMapping(".well-known/openid-configuration")
    @Produces("application/json")
    ResponseEntity<?> getOpenIdConnectDiscovery();


}
