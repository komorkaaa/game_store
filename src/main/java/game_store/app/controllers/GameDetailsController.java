package game_store.app.controllers;

import game_store.app.dao.CartDAO;
import game_store.app.models.Game;
import game_store.app.models.Session;
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

  private Game game; // Игра, информация о которой отображается

  public void setGame(Game game) {
    this.game = game;
    updateGameDetails();
  }

  /**
   * Обновление интерфейса данными выбранной игры
   */
  private void updateGameDetails() {
    // Устанавливаем основные данные игры
    titleLabel.setText(game.getTitle());
    genreLabel.setText(game.getGenreName());
    developerLabel.setText(game.getDeveloper());
    publisherLabel.setText(game.getPublisher());
    priceLabel.setText(String.format("%.2f ₽", game.getPrice()));

    // Устанавливаем описание
    if (game.getDescription() != null && !game.getDescription().isEmpty()) {
      descriptionArea.setText(game.getDescription());
    } else {
      descriptionArea.setText("Описание отсутствует");
    }

    // Загружаем изображение игры
    loadGameImage();
  }

  /**
   * Загрузка и отображение изображения игры
   */
  private void loadGameImage() {
    if (game.getImagePath() != null && !game.getImagePath().isEmpty()) {
      try {
        Image image = new Image(getClass().getResourceAsStream(game.getImagePath()));
        gameImage.setImage(image);
      } catch (Exception e) {
        System.out.println("Не удалось загрузить изображение: " + game.getImagePath());
        // Можно установить изображение по умолчанию
        setDefaultImage();
      }
    } else {
      setDefaultImage();
    }
  }

  /**
   * Установка изображения по умолчанию
   */
  private void setDefaultImage() {
    try {
      Image defaultImage = new Image(getClass().getResourceAsStream("/game_store/images/default_game.png"));
      gameImage.setImage(defaultImage);
    } catch (Exception e) {
      System.out.println("Не удалось загрузить изображение по умолчанию");
    }
  }

  /**
   * Обработчик кнопки "Добавить в корзину"
   */
  @FXML
  private void addToCart() {
    if (game != null) {
      CartDAO cartDAO = new CartDAO();
      cartDAO.addToCart(Session.getCurrentUserId(), game);

      showSuccessAlert("Игра '" + game.getTitle() + "' добавлена в корзину!");
    } else {
      showErrorAlert("Не выбрана игра для добавления в корзину");
    }
  }

  /**
   * Показать сообщение об успешном действии
   */
  private void showSuccessAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Корзина");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Показать сообщение об ошибке
   */
  private void showErrorAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Ошибка");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Закрытие окна детальной информации
   */
  @FXML
  private void closeWindow() {
    titleLabel.getScene().getWindow().hide();
  }

}