package mocks;

import com.identity.platform.utils.FileUtils;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

public class FileUtilsMock {

    @InjectMocks
    protected FileUtils fileUtils = new FileUtils();

    @BeforeTestMethod
    private void beforeTestMethod() {
        MockitoAnnotations.initMocks(this);
    }

    public static FileUtils getInstance() {
        FileUtilsMock fileUtilsMock = new FileUtilsMock();
        fileUtilsMock.beforeTestMethod();
        return (fileUtilsMock.fileUtils);
    }
}
