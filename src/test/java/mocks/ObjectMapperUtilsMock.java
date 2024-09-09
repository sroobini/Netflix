package mocks;

import com.identity.platform.utils.ObjectMapperUtils;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

public class ObjectMapperUtilsMock {

    @InjectMocks
    protected ObjectMapperUtils objectMapperUtils = new ObjectMapperUtils();

    @BeforeTestMethod
    private void beforeTestMethod() {
        MockitoAnnotations.initMocks(this);
    }

    public static ObjectMapperUtils getInstance() {
        ObjectMapperUtilsMock objectMapperUtilsMock = new ObjectMapperUtilsMock();
        objectMapperUtilsMock.beforeTestMethod();
        return (objectMapperUtilsMock.objectMapperUtils);
    }
}
