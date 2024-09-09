package mocks;

import com.identity.platform.utils.FileUtils;
import com.identity.platform.utils.JwtUtils;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

public class JwtUtilsMock {

    @InjectMocks
    protected JwtUtils jwtUtils = new JwtUtils();

    @Spy
    FileUtils fileUtils = FileUtilsMock.getInstance();

    @BeforeTestMethod
    private void beforeTestMethod() {
        MockitoAnnotations.initMocks(this);
    }

    public static JwtUtils getInstance() {
        JwtUtilsMock jwtUtilsMock = new JwtUtilsMock();
        jwtUtilsMock.beforeTestMethod();
        return (jwtUtilsMock.jwtUtils);
    }
}
