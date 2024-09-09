package com.identity.platform.utils;

import com.google.gson.Gson;
import com.identity.platform.model.api.OpenIDConnectResponse;
import com.identity.platform.model.common.Subject;
import com.identity.platform.model.common.User;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperUtils {

    public Subject mapToAuthPrincipal(User user) {
        if (user == null) {
            return null;
        }
        Subject authPrincipal = new Subject();
        authPrincipal.setId(user.getId());
        authPrincipal.setEmail(user.getEmail());
        authPrincipal.setUserName(user.getUserName());
        return  authPrincipal;
    }


    public OpenIDConnectResponse buildOpenIdConnectResponse(String openIdConnectString) {
            Gson gson = new Gson();
            return gson.fromJson(openIdConnectString, OpenIDConnectResponse.class);

    }
}
