package com.perebziak.pizzeriasystem.domain;

import com.perebziak.pizzeriasystem.domain.entity.Pizza;
import com.perebziak.pizzeriasystem.domain.entity.User;
import com.perebziak.pizzeriasystem.domain.enums.Role;
import com.perebziak.pizzeriasystem.domain.repository.PizzaDao;
import com.perebziak.pizzeriasystem.domain.repository.UserDao;

public class Main {

  public static void main(String[] args) {
    PizzaDao pizzaDao = new PizzaDao();
    UserDao userDao = new UserDao();

    // Тест запису
    pizzaDao.create(new Pizza("Пепероні", 250.0));
    userDao.create(new User("artem", "12345", Role.USER));

    // Тест читання
    System.out.println("Піци в JSON: " + pizzaDao.getAll());
    System.out.println("Користувачі в JSON: " + userDao.getAll());
  }
}