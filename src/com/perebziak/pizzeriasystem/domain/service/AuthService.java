package com.perebziak.pizzeriasystem.domain.service;

import com.perebziak.pizzeriasystem.domain.entity.User;
import com.perebziak.pizzeriasystem.domain.enums.Role;
import com.perebziak.pizzeriasystem.domain.repository.UserRepository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class AuthService {

  private final UserRepository userRepository;
  private String currentVerificationCode;
  private long codeExpirationTime;

  // === НАЛАШТУВАННЯ ПОШТИ (Впиши свої дані) ===
  private final String SENDER_EMAIL = "flars.xd@gmail.com";
  private final String APP_PASSWORD = "veef alnl bnlj khrx";

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User login(String login, String password) {
    User user = userRepository.searchByLogin(login);
    if (user != null && user.getPassword().equals(password)) {
      return user;
    }
    return null;
  }

  public boolean register(String login, String password, Role role) {
    if (userRepository.searchByLogin(login) != null) {
      return false;
    }
    userRepository.create(new User(login, password, role));
    return true;
  }

  // --- РЕАЛЬНА ВІДПРАВКА EMAIL ---
  public boolean sendRealEmailCode(String toEmail) {
    // Генеруємо 4-значний код
    currentVerificationCode = String.format("%04d", new Random().nextInt(10000));
    // Встановлюємо час життя коду (30 хвилин)
    codeExpirationTime = System.currentTimeMillis() + (30 * 60 * 1000);

    System.out.println(
        "\n\u001B[36m[Система]\u001B[0m ⏳ Підключення до серверів Google. Це може зайняти пару секунд...");

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
      }
    });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(SENDER_EMAIL));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
      message.setSubject("Код авторизації - Pizzeria System");
      message.setText("Ваш одноразовий код для входу: " + currentVerificationCode
          + "\n\nЦей код дійсний протягом 30 хвилин.");

      Transport.send(message);
      System.out.println(
          "\u001B[32m[Система]\u001B[0m ✅ Лист з кодом успішно відправлено на " + toEmail + "!\n");
      return true;
    } catch (MessagingException e) {
      System.out.println(
          "\u001B[31m[Помилка SMTP]\u001B[0m Не вдалося відправити лист. Перевірте пошту, пароль додатку та інтернет.");
      return false;
    }
  }

  // --- ПЕРЕВІРКА КОДУ ТА ЧАСУ ---
  public int verifyCode(String inputCode) {
    if (System.currentTimeMillis() > codeExpirationTime) {
      return -1; // Час вийшов (більше 30 хв)
    }
    if (currentVerificationCode != null && currentVerificationCode.equals(inputCode)) {
      return 1; // Все вірно
    }
    return 0; // Невірний код
  }
}