package game_store.app.controllers;

import game_store.app.models.Game;
import game_store.app.models.Session;
import game_store.app.services.CartService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameDetailsController {

  @FXML private Label titleLabel;
  @FXML private Label genreLabel;
  @FXML private Label priceLabel;
  @FXML private Label developerLabel;
  @FXML private Label publisherLabel;
  @FXML private TextArea descriptionArea;
  @FXML private ImageView gameImage;

  private final CartService cartService = new CartService();

  private Game game;

  public void setGame(Game game) {
    this.game = game;
    updateGameDetails();
  }

  private void updateGameDetails() {
    titleLabel.setText(game.getTitle());
    genreLabel.setText(game.getGenreName());
    developerLabel.setText(game.getDeveloper());
    publisherLabel.setText(game.getPublisher());
    priceLabel.setText(String.format("%.2f ₽", game.getPrice()));

    if (game.getDescription() != null && !game.getDescription().isEmpty()) {
      descriptionArea.setText(game.getDescription());
    } else {
      descriptionArea.setText("Описание отсутствует");
    }

    loadGameImage();
  }

  private void loadGameImage() {
    if (game.getImagePath() != null && !game.getImagePath().isEmpty()) {
      try {
        Image img = new Image(getClass().getResourceAsStream(game.getImagePath()));
        gameImage.setImage(img);
      } catch (Exception e) {
        System.out.println("Не удалось загрузить изображение: " + game.getImagePath());
        setDefaultImage();
      }
    } else {
      setDefaultImage();
    }
  }

  private void setDefaultImage() {
    try {
      Image img = new Image(getClass().getResourceAsStream("/game_store/images/default_game.png"));
      gameImage.setImage(img);
    } catch (Exception e) {
      System.out.println("Не удалось загрузить стандартное изображение");
    }
  }

  @FXML
  private void addToCart() {
    if (game == null) {
      showErrorAlert("Не выбрана игра для добавления");
      return;
    }

    try {
      cartService.addToCart(Session.getCurrentUserId(), game);
      showSuccessAlert("Игра '" + game.getTitle() + "' добавлена в корзину!");
    } catch (Exception e) {
      showErrorAlert("Ошибка при добавлении: " + e.getMessage());
    }
  }

  private void showSuccessAlert(String msg) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Корзина");
    alert.setHeaderText(null);
    alert.setContentText(msg);
    alert.showAndWait();
  }

  private void showErrorAlert(String msg) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Ошибка");
    alert.setHeaderText(null);
    alert.setContentText(msg);
    alert.showAndWait();
  }

  @FXML
  private void closeWindow() {
    titleLabel.getScene().getWindow().hide();
  }
}
