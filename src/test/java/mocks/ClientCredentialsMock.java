package mocks;

import com.identity.platform.dao.ClientCredentialsDAO;
import com.identity.platform.utils.FileUtils;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

public class ClientCredentialsMock {

    @InjectMocks
    protected ClientCredentialsDAO clientCredentialsDAO = new ClientCredentialsDAO();

    @Spy
    FileUtils fileUtils = FileUtilsMock.getInstance();

    @BeforeTestMethod
    private void beforeTestMethod() {
        MockitoAnnotations.initMocks(this);
    }

    public static ClientCredentialsDAO getInstance() {
        ClientCredentialsMock clientCredentialsMock = new ClientCredentialsMock();
        clientCredentialsMock.beforeTestMethod();
        return (clientCredentialsMock.clientCredentialsDAO);
    }
}
