package com.perebziak.pizzeriasystem.domain.ui.forms;

import com.perebziak.pizzeriasystem.domain.entity.Pizza;
import com.perebziak.pizzeriasystem.domain.ui.ConsoleHelper;
import java.util.Scanner;

public class AddPizzaForm {

  private final Scanner scanner = new Scanner(System.in);

  public Pizza render() {
    ConsoleHelper.printHeader("ДОДАВАННЯ ПІЦИ");
    System.out.print("Назва піци: ");
    String name = scanner.nextLine();
    System.out.print("Ціна: ");
    double price = Double.parseDouble(scanner.nextLine());
    return new Pizza(name, price);
  }
}