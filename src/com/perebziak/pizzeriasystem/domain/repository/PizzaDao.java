package com.perebziak.pizzeriasystem.domain.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.perebziak.pizzeriasystem.domain.entity.Pizza;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PizzaDao {

  private final String PATH = "data/pizzas.json";
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public PizzaDao() {
    new File("data").mkdirs();
  }

  public void create(Pizza pizza) {
    List<Pizza> list = getAll();
    list.add(pizza);
    try (Writer writer = new FileWriter(PATH)) {
      gson.toJson(list, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<Pizza> getAll() {
    File file = new File(PATH);
    if (!file.exists()) {
      return new ArrayList<>();
    }
    try (Reader reader = new FileReader(PATH)) {
      Type type = new TypeToken<ArrayList<Pizza>>() {
      }.getType();
      List<Pizza> list = gson.fromJson(reader, type);
      return list != null ? list : new ArrayList<>();
    } catch (IOException e) {
      return new ArrayList<>();
    }
  }
}