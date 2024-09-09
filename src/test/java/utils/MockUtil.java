package utils;

import com.identity.platform.model.api.OpenIDConnectResponse;
import com.identity.platform.model.common.Subject;
import com.identity.platform.model.common.TokenContext;

public class MockUtil {

    public static TokenContext getTokenContext() {
        TokenContext tokenContext = new TokenContext();
        Subject subject = new Subject();
        subject.setId("1234");
        subject.setEmail("test@test.com");
        subject.setUserName("test");
        tokenContext.setSubject(subject);
        tokenContext.setCodeChallenge("testCodeChallenge");
        tokenContext.setGsid("testGsid");
        tokenContext.setRedirectUri("testRedirectUri");
        tokenContext.setAuthCode("C21Code");
        return tokenContext;
    }

    public static OpenIDConnectResponse getOpenIDConnectResponse() {
        OpenIDConnectResponse openIdDiscoveryDoc = new OpenIDConnectResponse();
        openIdDiscoveryDoc.setIssuer("http://localhost:8080");
        openIdDiscoveryDoc.setJwks_uri("http://localhost:8080/oauth2/v1/certs");
        openIdDiscoveryDoc.setAuthorization_endpoint("http://localhost:8080/oauth2/v1/auth");
        openIdDiscoveryDoc.setToken_endpoint("http://localhost:8080/oauth2/v1/token");
        return openIdDiscoveryDoc;
    }
}
