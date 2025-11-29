package game_store.app.services;

import game_store.app.dao.UserDAO;

import java.time.LocalDate;
import java.time.Period;

public class RegisterService {

  private final UserDAO userDAO;

  public RegisterService(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  public String validate(
          String username, String email, String password,
          String confirm, String inn, String passport,
          LocalDate birthday, String phone
  ) {
    if (username.isEmpty() || email.isEmpty() || password.isEmpty()
            || inn.isEmpty() || passport.isEmpty()) {
      return "Заполните все поля";
    }

    if (!password.equals(confirm)) {
      return "Пароли не совпадают";
    }

    if (!username.matches("^[A-Za-z\\u0400-\\u04FF]{3,40}$")) {
      return "Не валидное имя пользователя";
    }

    if (!email.matches("^[\\w-\\.]+@[\\w-]+\\.([\\w-]+\\.)*[a-z]{2,}$")) {
      return "Невалидный email";
    }

    if (!phone.matches("^(\\+7)-\\d{3}-\\d{3}-\\d{2}-\\d{2}$")) {
      return "Невалидный номер телефона";
    }

    if (inn.length() != 12 || inn.startsWith("00")) {
      return "Неверный ИНН";
    }

    if (passport.length() != 10) {
      return "Неверные паспортные данные";
    }

    if (birthday == null) {
      return "Введите дату рождения";
    }

    int age = Period.between(birthday, LocalDate.now()).getYears();
    if (age < 16) {
      return "Пользователю нет 16 лет";
    }

    return "OK";
  }

  public String registerUser(
          String username, String email, String password,
          String confirm, String inn, String passport,
          LocalDate birthday, String phone
  ) {
    String validation = validate(username, email, password, confirm, inn, passport, birthday, phone);
    if (!validation.equals("OK")) {
      return validation;
    }

    boolean success = userDAO.registerUser(username, email, password, birthday, inn, passport, phone);
    if (!success) {
      return "Ошибка: логин или email уже используются";
    }

    return "SUCCESS";
  }
}
