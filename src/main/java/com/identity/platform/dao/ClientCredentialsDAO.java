package com.identity.platform.dao;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.identity.platform.error.CLIENT_CREDENTIALS_LOAD_ERROR;
import com.identity.platform.error.FILE_READING_ERROR;
import com.identity.platform.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.identity.platform.common.CONSTANTS.CLIENT_CREDENTIALS_JSON_FILE;

/**
 *  This DAO class is the access layer to Client credentials DB
 */
@Component
public class ClientCredentialsDAO {

    @Autowired
    FileUtils fileUtils;

    /**
     * This method loads the clientCredentials from the DB (ClientCredentials.json)
     * @return
     */
    public Map<String,String> loadClientCredentials() throws CLIENT_CREDENTIALS_LOAD_ERROR {
        Map<String,String> clientCredentials;
        try {
            String clientCredentialsStr = fileUtils.loadFileAsString(CLIENT_CREDENTIALS_JSON_FILE, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            clientCredentials = gson.fromJson(clientCredentialsStr, new TypeToken<Map<String, String>>() {
            }.getType());
        } catch (FILE_READING_ERROR error) {
            throw new CLIENT_CREDENTIALS_LOAD_ERROR(error.getMessage());
        }

        return clientCredentials;
    }

}
