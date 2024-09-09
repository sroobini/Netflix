package com.identity.platform.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.identity.platform.error.FILE_READING_ERROR;
import com.identity.platform.error.USER_LOAD_ERROR;
import com.identity.platform.model.common.User;
import com.identity.platform.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.identity.platform.common.CONSTANTS.USERS_JSON_FILE;

/**
 *   This DAO class is the access layer to User DB
 */
@Component
public class UserDAO {

    @Autowired
    FileUtils fileUtils;

    /**
     * This method load all the Users from the DB
     * @return
     * @throws USER_LOAD_ERROR
     */
    public Map<String, User> loadUsers() throws USER_LOAD_ERROR {
        Map<String, User> users;
        try {
            String userString = fileUtils.loadFileAsString(USERS_JSON_FILE, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            users = gson.fromJson(userString, new TypeToken<Map<String, User>>() {

            }.getType());
        }  catch (FILE_READING_ERROR error) {
            throw new USER_LOAD_ERROR(error.getMessage());
        }

        return users;
    }

    /**
     * This method fetches the User object from the loaded Users
     * @param userName
     * @return
     */
    public User fetchUser(String userName) throws USER_LOAD_ERROR {
        Map<String, User> users = loadUsers();
        return users.get(userName);
    }

}
