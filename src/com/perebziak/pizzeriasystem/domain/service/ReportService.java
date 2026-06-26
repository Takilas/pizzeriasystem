package com.perebziak.pizzeriasystem.domain.service;

import com.perebziak.pizzeriasystem.domain.entity.Pizza;
import com.perebziak.pizzeriasystem.domain.repository.PizzaRepository;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportService {

  private final PizzaRepository pizzaRepository;

  public ReportService(PizzaRepository pizzaRepository) {
    this.pizzaRepository = pizzaRepository;
  }

  public void exportPizzasToCSV() {
    String file = "data/pizzas_report.csv";
    try (FileWriter writer = new FileWriter(file)) {
      writer.append("ID,Назва піци,Ціна\n");
      List<Pizza> pizzas = pizzaRepository.getAll();
      for (Pizza p : pizzas) {
        writer.append(p.getId()).append(",").append(p.getName()).append(",")
            .append(String.valueOf(p.getPrice())).append("\n");
      }
      System.out.println("\n\u001B[32m✅ Звіт успішно згенеровано у файл: " + file + "\u001B[0m");
    } catch (IOException e) {
      System.out.println("Помилка генерації звіту: " + e.getMessage());
    }
  }
}