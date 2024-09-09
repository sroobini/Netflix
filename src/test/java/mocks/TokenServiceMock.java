package mocks;

import com.identity.platform.cache.AuthCache;
import com.identity.platform.service.TokenService;
import com.identity.platform.utils.JwtUtils;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import utils.MockUtil;

import static org.mockito.Mockito.when;

public class TokenServiceMock {

    @InjectMocks
    protected TokenService tokenService = new TokenService();

    @Spy
    AuthCache authCache = AuthCacheMock.getInstance();

    @Spy
    JwtUtils jwtUtils = JwtUtilsMock.getInstance();

    @BeforeTestMethod
    private void beforeTestMethod() {
        MockitoAnnotations.initMocks(this);
        when(authCache.get("Code")).thenReturn(MockUtil.getTokenContext());
    }

    public static TokenService getInstance() {
        TokenServiceMock tokenServiceMock = new TokenServiceMock();
        tokenServiceMock.beforeTestMethod();
        return (tokenServiceMock.tokenService);
    }
}
