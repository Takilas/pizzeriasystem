package com.perebziak.pizzeriasystem.domain.ui;

import com.perebziak.pizzeriasystem.domain.entity.User;
import com.perebziak.pizzeriasystem.domain.enums.Role;
import com.perebziak.pizzeriasystem.domain.repository.PizzaRepository;
import com.perebziak.pizzeriasystem.domain.repository.UserRepository;
import com.perebziak.pizzeriasystem.domain.service.AuthService;
import com.perebziak.pizzeriasystem.domain.ui.forms.LoginForm;
import com.perebziak.pizzeriasystem.domain.ui.pages.AdminView;
import com.perebziak.pizzeriasystem.domain.ui.pages.UserView;
import java.util.Scanner;

public class MainView {

  private final AuthService authService;
  private final PizzaRepository pizzaRepo;
  private final UserRepository userRepo;
  private final Scanner scanner = new Scanner(System.in);

  public MainView() {
    this.userRepo = new UserRepository();
    this.pizzaRepo = new PizzaRepository();
    this.authService = new AuthService(userRepo);

    // Адмін для тесту
    if (userRepo.searchByLogin("admin") == null) {
      userRepo.create(new User("admin", "admin", Role.ADMIN));
    }
  }

  public void start() {
    while (true) {
      ConsoleHelper.printHeader("ГОЛОВНЕ МЕНЮ");
      System.out.println("1. Увійти");
      System.out.println("2. Зареєструватися (Логіном має бути реальна E-mail адреса)");
      System.out.println("3. Вийти з програми");
      System.out.print("Вибір: ");

      String choice = scanner.nextLine();
      if (choice.equals("3")) {
        break;
      }

      // === ВХІД В АКАУНТ ===
      if (choice.equals("1")) {
        String[] creds = new LoginForm().render();
        User user = authService.login(creds[0], creds[1]);

        if (user == null) {
          System.out.println("❌ Невірний логін або пароль!");
        } else {
          // Адміна пускаємо без пошти, щоб ти міг перевіряти систему
          if (user.getRole() == Role.ADMIN) {
            System.out.println(
                ConsoleHelper.GREEN + "✅ Авторизація адміна успішна!" + ConsoleHelper.RESET);
            new AdminView(pizzaRepo).render();
            continue;
          }

          if (!authService.sendRealEmailCode(user.getLogin())) {
            continue;
          }

          System.out.print("Введіть код з листа: ");
          String inputCode = scanner.nextLine();

          int result = authService.verifyCode(inputCode);
          if (result == 1) {
            System.out.println(
                ConsoleHelper.GREEN + "✅ Авторизація успішна!" + ConsoleHelper.RESET);
            new UserView(pizzaRepo).render();
          } else {
            System.out.println("❌ Невірний код або час вийшов! Вхід скасовано.");
          }
        }
      }
      // === РЕЄСТРАЦІЯ АКАУНТА ===
      else if (choice.equals("2")) {
        System.out.println("⚠️ УВАГА: На вказаний Email буде відправлено код підтвердження!");
        String[] creds = new LoginForm().render();
        String email = creds[0];
        String password = creds[1];

        if (!email.contains("@")) {
          System.out.println("❌ Логін має бути справжньою поштою (містити @).");
          continue;
        }

        if (userRepo.searchByLogin(email) != null) {
          System.out.println("❌ Користувач з таким логіном вже існує.");
          continue;
        }

        // Відправляємо код для підтвердження реєстрації
        if (!authService.sendRealEmailCode(email)) {
          continue;
        }

        System.out.print("Введіть код підтвердження з листа: ");
        String inputCode = scanner.nextLine();

        int result = authService.verifyCode(inputCode);
        if (result == 1) {
          authService.register(email, password, Role.USER);
          System.out.println(
              ConsoleHelper.GREEN + "✅ Реєстрація успішна! Акаунт створено." + ConsoleHelper.RESET);
        } else {
          System.out.println("❌ Невірний код! Реєстрацію скасовано.");
        }
      }
    }
  }
}