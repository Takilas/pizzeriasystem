package com.perebziak.pizzeriasystem.domain.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.perebziak.pizzeriasystem.domain.entity.User;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

  private final String PATH = "data/users.json";
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public UserDao() {
    new File("data").mkdirs();
  }

  public void create(User user) {
    List<User> list = getAll();
    list.add(user);
    try (Writer writer = new FileWriter(PATH)) {
      gson.toJson(list, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<User> getAll() {
    File file = new File(PATH);
    if (!file.exists()) {
      return new ArrayList<>();
    }
    try (Reader reader = new FileReader(PATH)) {
      Type type = new TypeToken<ArrayList<User>>() {
      }.getType();
      List<User> list = gson.fromJson(reader, type);
      return list != null ? list : new ArrayList<>();
    } catch (IOException e) {
      return new ArrayList<>();
    }
  }
}