package com.perebziak.pizzeriasystem.domain.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perebziak.pizzeriasystem.domain.entity.User;
import com.perebziak.pizzeriasystem.domain.enums.Role;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRepository {

  private final String filePath = "data/users.json";
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private List<User> cache = new ArrayList<>(); // Наш IdentityMap (кеш)

  public UserRepository() {
    loadFromFile();
  }

  // --- СИНХРОНІЗАЦІЯ З ФАЙЛАМИ ---
  private void loadFromFile() {
    File file = new File(filePath);
    if (!file.exists()) {
      return;
    }
    try (Reader reader = new FileReader(file)) {
      User[] array = gson.fromJson(reader, User[].class);
      if (array != null) {
        cache = new ArrayList<>(Arrays.asList(array));
      }
    } catch (IOException e) {
      System.err.println("Помилка завантаження користувачів: " + e.getMessage());
    }
  }

  private void saveToFile() {
    try (Writer writer = new FileWriter(filePath)) {
      gson.toJson(cache, writer);
    } catch (IOException e) {
      System.err.println("Помилка збереження користувачів: " + e.getMessage());
    }
  }

  // --- CRUD ФУНКЦІЇ ---
  public void create(User user) {
    cache.add(user);
    saveToFile();
  }

  public List<User> getAll() {
    return new ArrayList<>(cache);
  }

  public User getById(String id) {
    return cache.stream()
        .filter(u -> u.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  public boolean update(String id, String newLogin, String newPassword, Role newRole) {
    User user = getById(id);
    if (user != null) {
      user.setLogin(newLogin);
      user.setPassword(newPassword);
      user.setRole(newRole);
      saveToFile();
      return true;
    }
    return false;
  }

  public boolean delete(String id) {
    boolean removed = cache.removeIf(u -> u.getId().equals(id));
    if (removed) {
      saveToFile();
    }
    return removed;
  }

  // --- ПОШУК ТА ФІЛЬТРАЦІЯ ---
  public synchronized User searchByLogin(String login) {
    return cache.stream()
        .filter(u -> u.getLogin().equalsIgnoreCase(login))
        .findFirst()
        .orElse(null);
  }

  public List<User> filterByRole(Role role) {
    List<User> result = new ArrayList<>();
    for (User u : cache) {
      if (u.getRole() == role) {
        result.add(u);
      }
    }
    return result;
  }
}