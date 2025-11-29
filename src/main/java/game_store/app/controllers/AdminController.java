package game_store.app.controllers;

import game_store.app.models.Game;
import game_store.app.services.GameAdminService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;

public class AdminController {

  @FXML private TableView<Game> gameTable;
  @FXML private TableColumn<Game, Integer> idColumn;
  @FXML private TableColumn<Game, String> titleColumn;
  @FXML private TableColumn<Game, String> genreColumn;
  @FXML private TableColumn<Game, Double> priceColumn;
  @FXML private TableColumn<Game, String> descriptionColumn;
  @FXML private TableColumn<Game, Date> releaseDateColumn;
  @FXML private TableColumn<Game, String> developerColumn;
  @FXML private TableColumn<Game, String> publisherColumn;

  @FXML private TextField titleField;
  @FXML private TextField genreField;
  @FXML private TextField priceField;
  @FXML private DatePicker releaseDatePicker;
  @FXML private TextField developerField;
  @FXML private TextField publisherField;
  @FXML private TextArea descriptionArea;

  @FXML private Button addButton;
  @FXML private Button updateButton;
  @FXML private Button deleteButton;
  @FXML private Button clearButton;

  private final GameAdminService gameService = new GameAdminService();
  private ObservableList<Game> gameList;

  @FXML
  public void initialize() {
    setupTableColumns();
    loadGames();
    setupSelectionListener();

    addButton.setOnAction(e -> handleAdd());
    updateButton.setOnAction(e -> handleUpdate());
    deleteButton.setOnAction(e -> handleDelete());
    clearButton.setOnAction(e -> clearForm());
  }

  private void setupTableColumns() {
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    genreColumn.setCellValueFactory(new PropertyValueFactory<>("genreName"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    releaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
    developerColumn.setCellValueFactory(new PropertyValueFactory<>("developer"));
    publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
  }

  private void loadGames() {
    gameList = FXCollections.observableArrayList(gameService.getAllGames());
    gameTable.setItems(gameList);
  }

  private void setupSelectionListener() {
    gameTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSel, newSel) -> showGameDetails(newSel)
    );
  }

  private void showGameDetails(Game game) {
    if (game != null) {
      titleField.setText(game.getTitle());
      genreField.setText(game.getGenreName());
      priceField.setText(String.valueOf(game.getPrice()));
      descriptionArea.setText(game.getDescription());
      releaseDatePicker.setValue(
              game.getReleaseDate() != null
                      ? game.getReleaseDate().toLocalDate()
                      : null
      );
      developerField.setText(game.getDeveloper());
      publisherField.setText(game.getPublisher());
    }
  }

  private void handleAdd() {
    try {
      gameService.addGame(
              titleField.getText(),
              genreField.getText(),
              Double.parseDouble(priceField.getText()),
              descriptionArea.getText(),
              releaseDatePicker.getValue() != null ? Date.valueOf(releaseDatePicker.getValue()) : null,
              developerField.getText(),
              publisherField.getText()
      );

      loadGames();
      clearForm();

    } catch (Exception e) {
      showError("Ошибка при добавлении игры: " + e.getMessage());
    }
  }

  private void handleUpdate() {
    Game selected = gameTable.getSelectionModel().getSelectedItem();
    if (selected == null) return;

    try {
      LocalDate date = releaseDatePicker.getValue();

      gameService.updateGame(
              selected,
              titleField.getText(),
              genreField.getText(),
              Double.parseDouble(priceField.getText()),
              descriptionArea.getText(),
              date != null ? Date.valueOf(date) : null,
              developerField.getText(),
              publisherField.getText()
      );

      loadGames();
      clearForm();

    } catch (Exception e) {
      showError("Ошибка при обновлении игры: " + e.getMessage());
    }
  }

  private void handleDelete() {
    Game selected = gameTable.getSelectionModel().getSelectedItem();
    if (selected == null) return;

    gameService.deleteGame(selected.getId());
    loadGames();
    clearForm();
  }

  private void clearForm() {
    titleField.clear();
    genreField.clear();
    priceField.clear();
    descriptionArea.clear();
    releaseDatePicker.setValue(null);
    developerField.clear();
    publisherField.clear();
  }

  @FXML
  public void openCatalog() {
    try {
      Stage stage = (Stage) gameTable.getScene().getWindow();
      Parent root = FXMLLoader.load(getClass().getResource("/game_store/catalog.fxml"));
      stage.setTitle("Каталог игр");
      stage.setScene(new Scene(root, 1000, 700));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Ошибка");
    alert.setHeaderText(null);
    alert.setContentText(msg);
    alert.showAndWait();
  }
}
