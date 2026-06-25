package com.perebziak.pizzeriasystem.domain.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perebziak.pizzeriasystem.domain.entity.Pizza;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PizzaRepository {

  private final String filePath = "data/pizzas.json";
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private List<Pizza> cache = new ArrayList<>(); // Наш IdentityMap (кеш)

  public PizzaRepository() {
    loadFromFile();
  }

  // --- СИНХРОНІЗАЦІЯ З ФАЙЛАМИ ---
  private void loadFromFile() {
    File file = new File(filePath);
    if (!file.exists()) {
      return;
    }
    try (Reader reader = new FileReader(file)) {
      Pizza[] array = gson.fromJson(reader, Pizza[].class);
      if (array != null) {
        cache = new ArrayList<>(Arrays.asList(array));
      }
    } catch (IOException e) {
      System.err.println("Помилка завантаження піц: " + e.getMessage());
    }
  }

  private void saveToFile() {
    try (Writer writer = new FileWriter(filePath)) {
      gson.toJson(cache, writer);
    } catch (IOException e) {
      System.err.println("Помилка збереження піц: " + e.getMessage());
    }
  }

  // --- CRUD ФУНКЦІЇ ---
  public void create(Pizza pizza) {
    cache.add(pizza);
    saveToFile();
  }

  public List<Pizza> getAll() {
    return new ArrayList<>(cache);
  }

  public Pizza getById(String id) {
    return cache.stream()
        .filter(p -> p.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  public boolean update(String id, String newName, double newPrice) {
    Pizza pizza = getById(id);
    if (pizza != null) {
      pizza.setName(newName);
      pizza.setPrice(newPrice);
      saveToFile();
      return true;
    }
    return false;
  }

  public boolean delete(String id) {
    boolean removed = cache.removeIf(p -> p.getId().equals(id));
    if (removed) {
      saveToFile();
    }
    return removed;
  }

  // --- ПОШУК ТА ФІЛЬТРАЦІЯ ---
  public List<Pizza> searchByName(String keyword) {
    List<Pizza> result = new ArrayList<>();
    for (Pizza p : cache) {
      if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
        result.add(p);
      }
    }
    return result;
  }

  public List<Pizza> filterByPrice(double min, double max) {
    List<Pizza> result = new ArrayList<>();
    for (Pizza p : cache) {
      if (p.getPrice() >= min && p.getPrice() <= max) {
        result.add(p);
      }
    }
    return result;
  }
}
