package com.perebziak.pizzeriasystem.domain.ui;

public class ConsoleHelper {

  public static final String RESET = "\u001B[0m";
  public static final String GREEN = "\u001B[32m";
  public static final String PURPLE = "\u001B[35m";
  public static final String CYAN = "\u001B[36m";

  public static void printHeader(String title) {
    System.out.println(PURPLE + "\n========== " + title + " ==========" + RESET);
  }

  public static void printTableRow(String col1, String col2, String col3) {
    System.out.printf(CYAN + "| %-36s | %-20s | %-10s |\n" + RESET, col1, col2, col3);
  }

  public static void printLine() {
    System.out.println(
        CYAN + "----------------------------------------------------------------------------"
            + RESET);
  }
}