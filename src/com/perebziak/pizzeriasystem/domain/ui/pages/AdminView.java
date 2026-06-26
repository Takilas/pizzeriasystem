package com.perebziak.pizzeriasystem.domain.ui.pages;

import com.perebziak.pizzeriasystem.domain.entity.Pizza;
import com.perebziak.pizzeriasystem.domain.repository.PizzaRepository;
import com.perebziak.pizzeriasystem.domain.service.ReportService;
import com.perebziak.pizzeriasystem.domain.ui.ConsoleHelper;
import com.perebziak.pizzeriasystem.domain.ui.forms.AddPizzaForm;
import java.util.Scanner;

public class AdminView {

  private final PizzaRepository pizzaRepo;
  private final ReportService reportService;
  private final Scanner scanner = new Scanner(System.in);

  public AdminView(PizzaRepository pizzaRepo) {
    this.pizzaRepo = pizzaRepo;
    this.reportService = new ReportService(pizzaRepo);
  }

  public void render() {
    while (true) {
      ConsoleHelper.printHeader("ПАНЕЛЬ АДМІНІСТРАТОРА");
      System.out.println("1. Переглянути меню піц (Таблиця)");
      System.out.println("2. Додати нову піцу");
      System.out.println("3. Експорт меню в Excel (CSV)");
      System.out.println("4. Вийти з акаунту");
      System.out.print("Вибір: ");

      String choice = scanner.nextLine();
      switch (choice) {
        case "1" -> showPizzas();
        case "2" -> {
          Pizza newPizza = new AddPizzaForm().render();
          pizzaRepo.create(newPizza);
          System.out.println(ConsoleHelper.GREEN + "✅ Піцу додано!" + ConsoleHelper.RESET);
        }
        case "3" -> reportService.exportPizzasToCSV();
        case "4" -> {
          return;
        }
        default -> System.out.println("Невірний вибір!");
      }
    }
  }

  private void showPizzas() {
    ConsoleHelper.printHeader("СТАН МЕНЮ (СКЛАД)");
    ConsoleHelper.printTableRow("ID", "Назва піци", "Ціна");
    ConsoleHelper.printLine();

    double total = 0;
    for (Pizza p : pizzaRepo.getAll()) {
      ConsoleHelper.printTableRow(p.getId(), p.getName(), p.getPrice() + " грн");
      total += p.getPrice();
    }
    ConsoleHelper.printLine();
    System.out.println(ConsoleHelper.GREEN + "ЗАГАЛЬНА ВАРТІСТЬ УСІХ ВИДІВ: " + total + " грн"
        + ConsoleHelper.RESET);
  }
}