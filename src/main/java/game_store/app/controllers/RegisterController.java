package game_store.app.controllers;

import game_store.app.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

  @FXML
  private TextField usernameField;

  @FXML
  private TextField emailField;

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

    if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
      messageLabel.setText("Заполните все поля");
      return;
    }

    if (!password.equals(confirmPassword)) {
      messageLabel.setText("Пароли не совпадают");
      return;
    }

    UserDAO dao = new UserDAO();
    boolean registrationSuccess = dao.registerUser(username, email, password);

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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
