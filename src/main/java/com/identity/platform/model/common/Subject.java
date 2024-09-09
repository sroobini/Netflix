package com.identity.platform.model.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Subject {

    public String id;

    public String userName;

    public String email;

    public String authClaim;
}
