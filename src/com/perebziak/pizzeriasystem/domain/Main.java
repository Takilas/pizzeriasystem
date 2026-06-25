package com.perebziak.pizzeriasystem.domain;

import com.perebziak.pizzeriasystem.domain.entity.Pizza;
import com.perebziak.pizzeriasystem.domain.entity.User;
import com.perebziak.pizzeriasystem.domain.enums.Role;
import com.perebziak.pizzeriasystem.domain.repository.PizzaRepository;
import com.perebziak.pizzeriasystem.domain.repository.UserRepository;
import net.datafaker.Faker;

import java.io.File;
import java.util.List;
import com.google.gson.Gson;

public class Main {

  public static void main(String[] args) {
    // Перевірка папки для даних
    File dataDir = new File("data");
    if (!dataDir.exists()) {
      dataDir.mkdir();
    }

    // Ініціалізуємо репозиторії
    PizzaRepository pizzaRepository = new PizzaRepository();
    UserRepository userRepository = new UserRepository();

    // Етап 2: Якщо бази порожні — згенеруємо початкові дані
    if (pizzaRepository.getAll().isEmpty()) {
      System.out.println("База даних порожня. Генеруємо початкові дані...");
      Faker faker = new Faker();
      for (int i = 0; i < 3; i++) {
        pizzaRepository.create(
            new Pizza(faker.food().dish() + " Pizza", faker.number().randomDouble(2, 150, 400)));
      }
      userRepository.create(new User("admin_artem", "superpass123", Role.ADMIN));
      userRepository.create(
          new User(faker.credentials().username(), faker.credentials().password(), Role.CUSTOMER));
    }

    System.out.println("\n=== ДЕМОНСТРАЦІЯ ЕТАПУ 3 (CRUD, Пошук, Фільтрація) ===");

    // 1. Тест READ (Отримання всіх)
    System.out.println("\nВсі піци в базі:");
    pizzaRepository.getAll().forEach(System.out::println);

    // 2. Тест CREATE (Створення нової піци)
    System.out.println("\n--- Додаємо нову фірмову піцу через Репозиторій ---");
    Pizza myPizza = new Pizza("Artem's Supreme Pizza", 350.00);
    pizzaRepository.create(myPizza);
    System.out.println("Додано успішно! ID нової піци: " + myPizza.getId());

    // 3. Тест SEARCH (Пошук за ключовим словом)
    System.out.println("\n--- Пошук піци, яка містить у назві 'Artem' ---");
    List<Pizza> searchResult = pizzaRepository.searchByName("Artem");
    searchResult.forEach(
        p -> System.out.println("Знайдено: " + p.getName() + " за " + p.getPrice() + " грн"));

    // 4. Тест FILTER (Фільтрація за ціною від 200 до 400 грн)
    System.out.println("\n--- Фільтрація піц у діапазоні цін 200 - 400 грн ---");
    List<Pizza> filteredPizzas = pizzaRepository.filterByPrice(200.00, 400.00);
    filteredPizzas.forEach(
        p -> System.out.println("Пройшла фільтр: " + p.getName() + " -> " + p.getPrice() + " грн"));

    // 5. Тест UPDATE (Оновлення ціни)
    System.out.println("\n--- Оновлюємо ціну для нашої фірмової піци ---");
    boolean isUpdated = pizzaRepository.update(myPizza.getId(), "Artem's Supreme Pizza (XL)",
        420.50);
    if (isUpdated) {
      System.out.println(
          "Оновлено! Нова назва та ціна: " + pizzaRepository.getById(myPizza.getId()));
    }

    // 6. Тест DELETE (Видалення)
    System.out.println("\n--- Видаляємо створену піцу з бази, щоб не забивати файл ---");
    boolean isDeleted = pizzaRepository.delete(myPizza.getId());
    if (isDeleted) {
      System.out.println("Піцу з ID " + myPizza.getId() + " успішно видалено з JSON файлу!");
    }

    System.out.println("\n=== ЕТАП 3 УСПІШНО ВИКОНАНО ===");
  }
}