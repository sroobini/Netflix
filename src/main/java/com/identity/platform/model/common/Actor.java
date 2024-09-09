package com.identity.platform.model.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Actor {

    public String clientId;
    public String client_type;
    public List<String> scopes;

}
