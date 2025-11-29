package game_store.app.controllers;

import game_store.app.dao.UserDAO;
import game_store.app.services.RegisterService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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

  private RegisterService registerService;

  @FXML
  public void initialize() {
    this.registerService = new RegisterService(new UserDAO());
  }

  @FXML
  private void register() {

    String result = registerService.registerUser(
            usernameField.getText().trim(),
            emailField.getText().trim(),
            passwordField.getText(),
            confirmField.getText(),
            innField.getText(),
            pasportField.getText(),
            birthdayPicker.getValue(),
            phoneField.getText()
    );

    if (result.equals("SUCCESS")) {
      messageLabel.setText("Аккаунт успешно создан");
      backToLogin();
    } else {
      messageLabel.setText(result);
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
