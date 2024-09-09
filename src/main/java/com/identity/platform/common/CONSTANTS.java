package com.identity.platform.common;

public interface CONSTANTS {
    String PRIVATE_KEY_FILE_PATH = "/PrivateKey.pem";
    String ISSUER = "http://localhost:8080";
    String AUD_URL = "http://localhost:3000";
    String AUTH_CODE_PREFIX = "C21";
    String SHA_ALGORITHM_NAME = "S256";
    String PKCE_AUTH_MECHANISM = "PKCE";

    String USERS_JSON_FILE = "/Users.json";
    String CLIENT_CREDENTIALS_JSON_FILE = "/ClientCredentials.json";


    String TEST_USER_NAME = "John12";
    String TEST_USER_PASSWORD = "xyz*123";

}
