package game_store.app.controllers;

import game_store.app.dao.DbConnection;
import game_store.app.dao.UserDAO;

import game_store.app.models.Session;
import game_store.app.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class LoginController {

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Label messageLabel;

  @FXML
  private void login() throws SQLException, ClassNotFoundException {
    String username = usernameField.getText().trim();
    String password = passwordField.getText();

    if (username.isEmpty() || password.isEmpty()) {
      messageLabel.setText("Введите логин и пароль");
      return;
    }

    DbConnection db = new DbConnection();

    if (!db.canConnect()) {
      messageLabel.setText("Ошибка подключения к базе данных");
      return;
    }

    // Аутентификация пользователя
    UserDAO dao = new UserDAO();
    User user = dao.authenticate(username, password);

    if (user != null) {
      Session.setCurrentUser(user);
      openCatalog();
    } else {
      messageLabel.setText("Неверный логин/пароль");
    }
  }

  public void openCatalog() {
    try {
      Stage currentStage = (Stage) usernameField.getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/game_store/catalog.fxml"));
      Parent root = loader.load();

      Stage catalogStage = new Stage();
      catalogStage.setTitle("Каталог игр");
      catalogStage.setScene(new Scene(root, 1000, 700));
      catalogStage.setMinWidth(900);
      catalogStage.setMinHeight(600);

      currentStage.close();
      catalogStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void openRegister() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/game_store/register.fxml"));
      Stage stage = (Stage) usernameField.getScene().getWindow();
      stage.setScene(new Scene(loader.load(), 500, 550));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void opeDBSettings() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/game_store/DBSettings.fxml"));
      Stage stage = (Stage) usernameField.getScene().getWindow();
      stage.setScene(new Scene(loader.load(), 500, 550));
      stage.centerOnScreen();
    } catch (Exception e) {
    e.printStackTrace();}
  }
}
