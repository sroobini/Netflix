package mocks;

import com.identity.platform.dao.ClientCredentialsDAO;
import com.identity.platform.dao.UserDAO;
import com.identity.platform.service.AuthenticationService;
import com.identity.platform.utils.ObjectMapperUtils;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

public class AuthenticationServiceMock {

    @InjectMocks
    protected AuthenticationService authenticationService = new AuthenticationService();

    @Spy
    UserDAO userDAO = UserDAOMock.getInstance();

    @Spy
    ClientCredentialsDAO clientCredentialsDAO = ClientCredentialsMock.getInstance();

    @Spy
    ObjectMapperUtils objectMapperUtils = ObjectMapperUtilsMock.getInstance();

    @BeforeTestMethod
    private void beforeTestMethod() {
        MockitoAnnotations.initMocks(this);
    }

    public static AuthenticationService getInstance() {
        AuthenticationServiceMock authenticationServiceMock = new AuthenticationServiceMock();
        authenticationServiceMock.beforeTestMethod();
        return (authenticationServiceMock.authenticationService);
    }
}
