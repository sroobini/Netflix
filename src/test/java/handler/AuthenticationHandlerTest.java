package handler;

import com.identity.platform.cache.AuthCache;
import com.identity.platform.handler.AuthenticationHandler;
import com.identity.platform.model.api.ErrorResponse;
import com.identity.platform.model.api.LoginRequest;
import com.identity.platform.model.api.TokenRequest;
import com.identity.platform.model.api.TokenResponse;
import com.identity.platform.model.common.TokenContext;
import com.identity.platform.service.AuthenticationService;
import com.identity.platform.service.TokenService;
import com.identity.platform.utils.FileUtils;
import com.identity.platform.utils.ObjectMapperUtils;
import mocks.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestMethod;


import mocks.FileUtilsMock;
import mocks.ObjectMapperUtilsMock;

import utils.MockUtil;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AuthenticationHandlerTest.class})
@RunWith(MockitoJUnitRunner.class)
class AuthenticationHandlerTest {

    @InjectMocks
    private AuthenticationHandler authenticationHandler = new AuthenticationHandler();

    @Spy
    FileUtils fileUtils =  FileUtilsMock.getInstance();

    @Spy
    ObjectMapperUtils objectMapperUtils = ObjectMapperUtilsMock.getInstance();

    @Spy
    AuthenticationService authenticationService = AuthenticationServiceMock.getInstance();

    @Spy
    TokenService tokenService = TokenServiceMock.getInstance();

    @Spy
    AuthCache authCache = AuthCacheMock.getInstance();

    @BeforeTestMethod
    private void beforeTestMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAuthenticateLogin() throws Exception {
        String headers = null;
        LoginRequest loginRequest = new LoginRequest("testCodeChallenge", "S256",
                "code", "query", "testRedirectUri", "openid");
        // Perform the request
        ResponseEntity response = authenticationHandler.authenticateLogin(headers, loginRequest);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().toString().contains("testRedirectUri?code=C21"));
    }

    @Test
    public void testAuthenticateLoginMissingInput() throws Exception {
        String headers = null;
        LoginRequest loginRequest = new LoginRequest(null, null,
                "code", "query", "testRedirectUri", "openid");
        // Perform the request
        ResponseEntity response = authenticationHandler.authenticateLogin(headers, loginRequest);
        Assertions.assertEquals(401, response.getStatusCodeValue());
        Assertions.assertNotNull(response.getBody());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        Assertions.assertEquals(errorResponse.getErrorMessage(), "Missing Input Parameter");
    }

    @Test
    public void testCreateToken() throws Exception {
        String authHeader = null;
        TokenRequest tokenRequest = new TokenRequest("C21Code", "authorization_code",
                "testCodeVerifier", "testRedirectUri");
        // Perform the request
        TokenContext tokenContext = MockUtil.getTokenContext();
        when(tokenService.getTokenContextFromCode("C21Code")).thenReturn(tokenContext);
        when(authCache.get("Code")).thenReturn(tokenContext);
        when(tokenService.validateCodeVerifier(tokenRequest, tokenContext)).thenReturn(true);
        ResponseEntity response = authenticationHandler.createToken(authHeader, tokenRequest);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertNotNull(response.getBody());
        TokenResponse tokenResponse = (TokenResponse) response.getBody();
        Assertions.assertNotNull(tokenResponse.getIdToken());

    }

    @Test
    public void testCreateTokenInvalidCodeVerifier() throws Exception {
        String authHeader = null;
        TokenRequest tokenRequest = new TokenRequest("C21Code", "authorization_code",
                "invalidCodeVerifier", "testRedirectUri");
        // Perform the request
        ResponseEntity response = authenticationHandler.createToken(authHeader, tokenRequest);
        Assertions.assertEquals(401, response.getStatusCodeValue());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        Assertions.assertEquals(errorResponse.getErrorMessage(), "Invalid code verifier");

    }

}