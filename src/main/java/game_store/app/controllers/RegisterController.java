package game_store.app.controllers;

import game_store.app.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class RegisterController {

  @FXML
  private TextField usernameField;

  @FXML
  private TextField emailField;

  @FXML
  private TextField innField;

  @FXML
  private TextField pasportField;

  @FXML
  private DatePicker birthdayPicker;

  @FXML
  private TextField phoneField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private PasswordField confirmField;

  @FXML
  private Label messageLabel;


  @FXML
  private void register() {
    String username = usernameField.getText().trim();
    String email = emailField.getText().trim();
    String password = passwordField.getText();
    String confirmPassword = confirmField.getText();
    String inn = innField.getText();
    String pasport = pasportField.getText();
    LocalDate birthday = birthdayPicker.getValue();
    String phone_number = phoneField.getText();

    if (username.isEmpty() || email.isEmpty() || password.isEmpty() || inn.isEmpty() || pasport.isEmpty()) {
      messageLabel.setText("Заполните все поля");
      return;
    }

    if (!password.equals(confirmPassword)) {
      messageLabel.setText("Пароли не совпадают");
      return;
    }

    String LOGIN_REGEX = "^[A-Za-z\\u0400-\\u04FF]{3,40}$";

    if (!username.matches(LOGIN_REGEX)) {
      messageLabel.setText("Не валидное имя пользователя");
      return;
    }

    String EMAIL_REGEX = "^[\\w-\\.]+@[\\w-]+\\.([\\w-]+\\.)*[a-z]{2,}$";

    if (!email.matches(EMAIL_REGEX)) {
      messageLabel.setText("Невалидный email");
      return;
    }

    String PHONE_REGEX = "^(\\+7)-\\d{3}-\\d{3}-\\d{2}-\\d{2}$";

    if (!phone_number.matches(PHONE_REGEX)) {
      messageLabel.setText("Невалидный номер телефона");
      return;
    }

    if (inn.length() != 12 || inn.startsWith("00")) {
      messageLabel.setText("Неверный ИНН");
      return;
    }

    if (pasport.length() != 10) {
      messageLabel.setText("Невернные паспортные данные");
      return;
    }

    LocalDate currentDate = LocalDate.now();
    Period period = Period.between(birthday, currentDate);
    int age = period.getYears();

    if (age < 16) {
      messageLabel.setText("Пользователю нет 16 лет");
      return;
    }

    UserDAO dao = new UserDAO();
    boolean registrationSuccess = dao.registerUser(username, email, password, birthday, inn, pasport, phone_number);

    if (registrationSuccess) {
      messageLabel.setText("Аккаунт успешно создан");
      backToLogin();
    } else {
      messageLabel.setText("Ошибка: логин или email уже используются");
    }
  }

  @FXML
  private void backToLogin() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/game_store/login.fxml"));
      Stage stage = (Stage) usernameField.getScene().getWindow();
      stage.setScene(new Scene(loader.load(), 500, 500));
      stage.centerOnScreen();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
