package com.identity.platform.handler;

import com.identity.platform.api.IMetadata;
import com.identity.platform.error.FILE_READING_ERROR;
import com.identity.platform.model.api.ErrorResponse;
import com.identity.platform.model.api.IResponse;
import com.identity.platform.model.api.OpenIDConnectResponse;
import com.identity.platform.utils.FileUtils;
import com.identity.platform.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class MetadataHandler implements IMetadata {

    public static final String OPEN_ID_CONFIG_JSON_FILE = "/OpenIdConfig.json";
    public static final String JWKS_JSON_FILE = "/PublicKeys.json";

    @Autowired
    FileUtils fileUtils;

    @Autowired
    ObjectMapperUtils objectMapperUtils;

    @Override
    public ResponseEntity<IResponse> getOpenIdConnectDiscovery() {
        String openIdConnectConfig;
        OpenIDConnectResponse response;
        try {
            openIdConnectConfig = fileUtils.loadFileAsString(OPEN_ID_CONFIG_JSON_FILE, StandardCharsets.UTF_8);
            response = objectMapperUtils.buildOpenIdConnectResponse(openIdConnectConfig);
        } catch(FILE_READING_ERROR fileReadingError) {
            return new ResponseEntity<IResponse>(
                    new ErrorResponse(fileReadingError.getMessage(),
                            "Unable to Load OpenId Config doc"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getJwksConfig() {
        String jwksConfig;

        try {
            jwksConfig = fileUtils.loadFileAsString(JWKS_JSON_FILE, StandardCharsets.UTF_8);

        } catch(FILE_READING_ERROR fileReadingError) {
            return new ResponseEntity<ErrorResponse>(new ErrorResponse(fileReadingError.getMessage(),
                    "Unable to Load JWKS Config doc"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(jwksConfig);
    }

}
