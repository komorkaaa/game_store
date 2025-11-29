package game_store.app.controllers;

import game_store.app.services.DBSettingsService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DBSettingsController {

  @FXML private Button button;

  @FXML private TextField HOST;
  @FXML private TextField PORT;
  @FXML private TextField DB_NAME;
  @FXML private TextField LOGIN;
  @FXML private PasswordField PASS;

  @FXML private Label messageLabel;

  private final DBSettingsService settingsService = new DBSettingsService();

  @FXML
  public void initialize() {
    HOST.setText(settingsService.getHost());
    PORT.setText(settingsService.getPort());
    DB_NAME.setText(settingsService.getDbName());
    LOGIN.setText(settingsService.getLogin());
    PASS.setText(settingsService.getPassword());
  }

  @FXML
  private void save() {
    try {
      settingsService.saveSettings(
              HOST.getText(),
              PORT.getText(),
              DB_NAME.getText(),
              LOGIN.getText(),
              PASS.getText()
      );

      messageLabel.setText("Изменения сохранены");
    } catch (Exception e) {
      messageLabel.setText("Ошибка сохранения");
      e.printStackTrace();
    }
  }

  @FXML
  private void backToLogin() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/game_store/login.fxml"));
      Stage stage = (Stage) button.getScene().getWindow();
      stage.setScene(new Scene(loader.load(), 500, 550));
      stage.centerOnScreen();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
