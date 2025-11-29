package game_store.app.controllers;

import game_store.app.dao.CartDAO;
import game_store.app.dao.GameDAO;
import game_store.app.dao.GenreDAO;
import game_store.app.models.Game;
import game_store.app.models.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CatalogController {


  @FXML private TableView<Game> gameTable;
  @FXML private TableColumn<Game, String> nameColumn;
  @FXML private TableColumn<Game, String> genreColumn;
  @FXML private TableColumn<Game, Double> priceColumn;

  @FXML private ComboBox<String> genreComboBox;
  @FXML private TextField searchField;
  @FXML private Button adminButton;

  private final GameDAO gameDAO = new GameDAO();
  private final GenreDAO genreDAO = new GenreDAO();

  @FXML
  public void initialize() {
    setupTableColumns();
    loadGenres();
    loadGames();
    setupDoubleClickHandler();

    adminButton.setVisible(Session.isAdmin());
  }

  private void setupTableColumns() {
    nameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getTitle()));
    genreColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getGenreName()));
    priceColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getPrice()));
  }

  private void loadGenres() {
    genreComboBox.getItems().add("Все жанры");
    genreComboBox.getItems().addAll(genreDAO.getAllGenres());
    genreComboBox.getSelectionModel().selectFirst();
    genreComboBox.setOnAction(event -> filterGames());
  }

  private void loadGames() {
    gameTable.getItems().setAll(gameDAO.getAllGames());
  }

  private void filterGames() {
    String selected = genreComboBox.getValue();
    if (selected == null || selected.equals("Все жанры")) {
      loadGames();
    } else {
      int genreId = genreDAO.getGenreIdByName(selected);
      gameTable.getItems().setAll(gameDAO.getGamesByGenre(genreId));
    }
  }

  @FXML
  private void onSearch() {
    String keyword = searchField.getText().trim();
    if (keyword.isEmpty()) {
      filterGames();
    } else {
      gameTable.getItems().setAll(gameDAO.searchGamesByName(keyword));
    }
  }

  @FXML
  private void addToCart() {
    Game selected = gameTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
      CartDAO cartDAO = new CartDAO();
      cartDAO.addToCart(Session.getCurrentUserId(), selected);

      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Корзина");
      alert.setHeaderText(null);
      alert.setContentText("Игра добавлена в корзину!");
      alert.showAndWait();
    }
  }

  @FXML
  private void openCart() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/game_store/cart.fxml"));
      Stage stage = new Stage();
      stage.setTitle("Корзина");
      stage.setScene(new Scene(loader.load(), 600, 500));
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void openOrders() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/game_store/orders.fxml"));
      Parent root = loader.load();
      Stage stage = new Stage();
      stage.setTitle("История заказов");
      stage.setScene(new Scene(root, 600, 400));
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void backToLoginFromCatalog() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/game_store/login.fxml"));
      Stage stage = (Stage) gameTable.getScene().getWindow();
      stage.setScene(new Scene(loader.load(), 500, 500));
      stage.setTitle("Game Store");
      stage.centerOnScreen();

      // Очищаем сессию при выходе
      Session.clear();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void openGameDetails(Game game) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/game_store/game_details.fxml"));
      Parent root = loader.load();

      GameDetailsController controller = loader.getController();
      controller.setGame(game); // ← Только один параметр

      Stage stage = new Stage();
      stage.setTitle("Информация об игре: " + game.getTitle());
      stage.setScene(new Scene(root, 500, 550));
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
      showAlert("Ошибка", "Не удалось открыть информацию об игре");
    }
  }

  private void setupDoubleClickHandler() {
    gameTable.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2 && !gameTable.getSelectionModel().isEmpty()) {
        Game selectedGame = gameTable.getSelectionModel().getSelectedItem();
        openGameDetails(selectedGame);
      }
    });
  }

  public void openAdminPanel() {
    try {
      Stage stage = (Stage) adminButton.getScene().getWindow();
      Parent root = FXMLLoader.load(getClass().getResource("/game_store/admin.fxml"));
      stage.setTitle("Админ-панель");
      stage.setScene(new Scene(root, 1200, 700));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showAlert(String title, String msg) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setContentText(msg);
    alert.showAndWait();
  }
}
