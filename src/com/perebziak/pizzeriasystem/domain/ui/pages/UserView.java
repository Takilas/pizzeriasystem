package com.perebziak.pizzeriasystem.domain.ui.pages;

import com.perebziak.pizzeriasystem.domain.entity.Pizza;
import com.perebziak.pizzeriasystem.domain.repository.PizzaRepository;
import com.perebziak.pizzeriasystem.domain.ui.ConsoleHelper;
import java.util.List;
import java.util.Scanner;

public class UserView {

  private final PizzaRepository pizzaRepo;
  private final Scanner scanner = new Scanner(System.in);

  public UserView(PizzaRepository pizzaRepo) {
    this.pizzaRepo = pizzaRepo;
  }

  public void render() {
    while (true) {
      ConsoleHelper.printHeader("МЕНЮ КЛІЄНТА");
      System.out.println("1. Зробити замовлення");
      System.out.println("2. Вийти з акаунту");
      System.out.print("Вибір: ");

      String choice = scanner.nextLine();
      if (choice.equals("2")) {
        return;
      }
      if (choice.equals("1")) {
        makeOrder();
      }
    }
  }

  private void makeOrder() {
    double cartTotal = 0;

    while (true) {
      ConsoleHelper.printHeader("ДОСТУПНІ ПІЦИ");
      List<Pizza> pizzas = pizzaRepo.getAll();

      if (pizzas.isEmpty()) {
        System.out.println("Вибачте, меню наразі порожнє.");
        return;
      }

      for (int i = 0; i < pizzas.size(); i++) {
        Pizza p = pizzas.get(i);
        System.out.println((i + 1) + ". " + p.getName() + " - " + p.getPrice() + " грн");
      }
      System.out.println("0. Повернутися в головне меню");

      System.out.print("\nВведіть номер піци (або 0 для виходу): ");
      int pizzaIndex;
      try {
        pizzaIndex = Integer.parseInt(scanner.nextLine());
        if (pizzaIndex == 0) {
          return; // Вихід в головне меню
        }
        pizzaIndex -= 1; // Коригуємо індекс для масиву
        if (pizzaIndex < 0 || pizzaIndex >= pizzas.size()) {
          throw new Exception();
        }
      } catch (Exception e) {
        System.out.println("❌ Невірний вибір!");
        continue;
      }

      Pizza selectedPizza = pizzas.get(pizzaIndex);

      System.out.print("Скільки таких піц ви хочете? : ");
      int quantity;
      try {
        quantity = Integer.parseInt(scanner.nextLine());
        if (quantity <= 0) {
          throw new Exception();
        }
      } catch (Exception e) {
        System.out.println("❌ Невірна кількість!");
        continue;
      }

      cartTotal += selectedPizza.getPrice() * quantity;
      System.out.println(
          ConsoleHelper.GREEN + "✅ Додано в кошик: " + selectedPizza.getName() + " x" + quantity
              + ConsoleHelper.RESET);

      while (true) {
        System.out.println("\nПоточна сума кошика: " + cartTotal + " грн");
        System.out.println("1. Додати ще піцу");
        System.out.println("2. Оформити замовлення");
        System.out.println("3. Скасувати замовлення та вийти в меню");
        System.out.print("Вибір: ");
        String nextChoice = scanner.nextLine();

        if (nextChoice.equals("1")) {
          break;
        } else if (nextChoice.equals("2")) {
          ConsoleHelper.printHeader("ЧЕК ЗАМОВЛЕННЯ");
          System.out.println("До сплати: " + cartTotal + " грн");
          System.out.println(ConsoleHelper.GREEN + "✅ Замовлення виконано! Очікуйте видачі."
              + ConsoleHelper.RESET);
          return;
        } else if (nextChoice.equals("3")) {
          System.out.println("🛑 Замовлення скасовано.");
          return; // Вихід без збереження замовлення
        } else {
          System.out.println("❌ Невірний вибір!");
        }
      }
    }
  }
}