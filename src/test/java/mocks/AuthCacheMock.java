package mocks;

import com.identity.platform.cache.AuthCache;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

public class AuthCacheMock {

    @InjectMocks
    protected AuthCache authCache = new AuthCache();

    @BeforeTestMethod
    private void beforeTestMethod() {
        MockitoAnnotations.initMocks(this);
    }

    public static AuthCache getInstance() {
        AuthCacheMock authCacheMock = new AuthCacheMock();
        authCacheMock.beforeTestMethod();
        return (authCacheMock.authCache);
    }


}
