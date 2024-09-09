package com.identity.platform.model.common;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class User {
  private String id;
  private String userName;
  private String email;
  private String passwordHash;

  public User(String id, String userName, String email, String passwordHash) {
    this.id = id;
    this.userName = userName;
    this.email = email;
    this.passwordHash = passwordHash;
  }

}
