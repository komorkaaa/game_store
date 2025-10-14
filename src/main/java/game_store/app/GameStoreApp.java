package game_store.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameStoreApp extends Application {

  @Override
  public void start(Stage stage) throws Exception {

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/game_store/login.fxml"));
    Scene scene = new Scene(loader.load(), 500, 500);
    stage.setScene(scene);
    stage.setTitle("Game Store");
    stage.setMinWidth(450);
    stage.setMinHeight(450);
    stage.centerOnScreen();
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

