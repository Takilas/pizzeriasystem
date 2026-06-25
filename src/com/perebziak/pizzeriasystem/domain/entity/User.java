package com.perebziak.pizzeriasystem.domain.entity;

import com.perebziak.pizzeriasystem.domain.enums.Role;
import java.util.UUID;

public class User {

  private String id;
  private String login;
  private String password;
  private Role role;

  public User(String login, String password, Role role) {
    this.id = UUID.randomUUID().toString();
    setLogin(login);
    setPassword(password);
    setRole(role);
  }

  public void setLogin(String login) {
    if (login == null || login.trim().length() < 3) {
      throw new IllegalArgumentException("Логін має містити мінімум 3 символи");
    }
    this.login = login;
  }

  public void setPassword(String password) {
    if (password == null || password.trim().length() < 4) {
      throw new IllegalArgumentException("Пароль має містити мінімум 4 символи");
    }
    this.password = password;
  }

  public void setRole(Role role) {
    if (role == null) {
      throw new IllegalArgumentException("Роль не може бути порожньою");
    }
    this.role = role;
  }

  public String getId() {
    return id;
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  public Role getRole() {
    return role;
  }

  @Override
  public String toString() {
    return "User{" +
        "login='" + login + '\'' +
        ", role=" + role +
        '}';
  }
}