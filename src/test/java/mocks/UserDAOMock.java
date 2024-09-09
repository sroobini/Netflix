package mocks;

import com.identity.platform.dao.UserDAO;
import com.identity.platform.utils.FileUtils;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

public class UserDAOMock {

    @InjectMocks
    protected UserDAO userDAO = new UserDAO();

    @Spy
    FileUtils fileUtils = FileUtilsMock.getInstance();

    @BeforeTestMethod
    private void beforeTestMethod() {
        MockitoAnnotations.initMocks(this);
    }

    public static UserDAO getInstance() {
        UserDAOMock userDAOMock = new UserDAOMock();
        userDAOMock.beforeTestMethod();
        return (userDAOMock.userDAO);
    }
}
