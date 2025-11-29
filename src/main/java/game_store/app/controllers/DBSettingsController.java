package game_store.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class DBSettingsController {

  @FXML
  private Button button;

  @FXML TextField HOST;
  @FXML TextField PORT;
  @FXML TextField DB_NAME;
  @FXML TextField LOGIN;
  @FXML PasswordField PASS;

  @FXML
  public Label messageLabel;

  public Preferences userData = Preferences.userRoot().node("pref");

  @FXML
  public void initialize() {
    HOST.setText(userData.get("host", "host"));
    PORT.setText(userData.get("port", "port"));
    DB_NAME.setText(userData.get("db_name", "db_name"));
    LOGIN.setText(userData.get("login", "login"));
    PASS.setText(userData.get("pass", "pass"));

  }

  @FXML
  private void save() {
    try {
      userData.put("host", HOST.getText());
      userData.put("port", PORT.getText());
      userData.put("db_name", DB_NAME.getText());
      userData.put("login", LOGIN.getText());
      userData.put("pass", PASS.getText());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    messageLabel.setText("Изменения сохранены");
  }

  @FXML
  private void backToLogin() throws IOException {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/game_store/login.fxml"));
      Stage stage = (Stage) button.getScene().getWindow();
      stage.setScene(new Scene(loader.load(), 500, 550));
      stage.centerOnScreen();
    } catch (Exception e) {
      e.printStackTrace();}
  }
}
