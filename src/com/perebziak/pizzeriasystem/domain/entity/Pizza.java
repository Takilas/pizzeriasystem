package com.perebziak.pizzeriasystem.domain.entity;

import java.util.UUID;

public class Pizza {

  private String id;
  private String name;
  private double price;

  public Pizza(String name, double price) {
    this.id = UUID.randomUUID().toString();
    setName(name);
    setPrice(price);
  }

  public void setName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Назва піци не може бути порожньою");
    }
    this.name = name;
  }

  public void setPrice(double price) {
    if (price <= 0) {
      throw new IllegalArgumentException("Ціна має бути більшою за нуль");
    }
    this.price = price;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getPrice() {
    return price;
  }

  @Override
  public String toString() {
    return "Pizza{" +
        "name='" + name + '\'' +
        ", price=" + price +
        '}';
  }
}