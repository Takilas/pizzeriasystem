package com.perebziak.pizzeriasystem.domain.ui.forms;

import com.perebziak.pizzeriasystem.domain.ui.ConsoleHelper;
import java.util.Scanner;

public class LoginForm {

  private final Scanner scanner = new Scanner(System.in);

  public String[] render() {
    ConsoleHelper.printHeader("АВТОРИЗАЦІЯ");
    System.out.print("Логін: ");
    String login = scanner.nextLine();
    System.out.print("Пароль: ");
    String password = scanner.nextLine();
    return new String[]{login, password};
  }
}
