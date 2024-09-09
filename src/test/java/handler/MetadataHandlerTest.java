package handler;

import com.identity.platform.handler.MetadataHandler;
import com.identity.platform.model.api.OpenIDConnectResponse;
import com.identity.platform.utils.FileUtils;
import com.identity.platform.utils.ObjectMapperUtils;
import mocks.FileUtilsMock;
import mocks.ObjectMapperUtilsMock;
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
import utils.MockUtil;



@SpringBootTest(classes = {MetadataHandlerTest.class})
@RunWith(MockitoJUnitRunner.class)
class MetadataHandlerTest {

    @InjectMocks
    private MetadataHandler metadataHandler = new MetadataHandler();

    @Spy
    FileUtils fileUtils =  FileUtilsMock.getInstance();

    @Spy
    ObjectMapperUtils objectMapperUtils = ObjectMapperUtilsMock.getInstance();

    @BeforeTestMethod
    private void beforeTestMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOpenIdConnectDiscovery() throws Exception {
        // Mock service behavior
        OpenIDConnectResponse openIdDiscoveryDoc = MockUtil.getOpenIDConnectResponse();
        // Perform the request
        ResponseEntity response = metadataHandler.getOpenIdConnectDiscovery();
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertNotNull(response.getBody());
        OpenIDConnectResponse openIdDiscoveryResponse = (OpenIDConnectResponse) response.getBody();
        Assertions.assertEquals(openIdDiscoveryDoc.getIssuer(), openIdDiscoveryResponse.getIssuer());
        Assertions.assertEquals(openIdDiscoveryDoc.getJwks_uri(), openIdDiscoveryResponse.getJwks_uri());
        Assertions.assertEquals(openIdDiscoveryDoc.getAuthorization_endpoint(),
                openIdDiscoveryResponse.getAuthorization_endpoint());
        Assertions.assertEquals(openIdDiscoveryDoc.getToken_endpoint(), openIdDiscoveryResponse.getToken_endpoint());

    }

    @Test
    public void testGetJwksConfig() throws Exception {
        // Mock service behavior
         String publicKeys = "{\n" +
                 "\"keys\" :[\n" +
                 "    {\n" +
                 "      \"crv\": \"P-256\",\n" +
                 "      \"ext\": true,\n" +
                 "      \"key_ops\": [\n" +
                 "        \"verify\"\n" +
                 "      ],\n" +
                 "      \"kid\": \"J2MkveAA6eeBhy1TU7hbSCOLmMMeUcr_pGo8UHk0G_4\",\n" +
                 "      \"kty\": \"EC\",\n" +
                 "      \"x\": \"BggDtfzEZdYJHgwfpmRDrcnnj4aW-xE4V3WRUZ1ZZVI\",\n" +
                 "      \"y\": \"jdFgGI6jCRrSeRi2Gj0ZEeIufYrx2N8jebvdS2ossTs\"\n" +
                 "    }\n" +
                 "  ]\n" +
                 "}";
        // Perform the request
        ResponseEntity response = metadataHandler.getJwksConfig();
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertNotNull(response.getBody());
        String responseBody = response.getBody().toString();
        Assertions.assertEquals(responseBody, publicKeys);
    }
}