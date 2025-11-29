package game_store.app.controllers;

import game_store.app.dao.CartDAO;
import game_store.app.dao.OrderDAO;
import game_store.app.models.CartItem;
import game_store.app.models.OrderItem;
import game_store.app.models.Session;
import game_store.app.services.CartService;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CartController {

  @FXML private TableView<CartItem> cartTable;
  @FXML private TableColumn<CartItem, String> nameColumn;
  @FXML private TableColumn<CartItem, Double> priceColumn;
  @FXML private TableColumn<CartItem, Integer> quantityColumn;
  @FXML private TableColumn<CartItem, Double> totalColumn;
  @FXML private Label totalLabel;

  private CartService cartService; // теперь сервис
  private int currentUserId = Session.getCurrentUserId();

  // Конструктор для DI (для тестов можно передавать мок)
  public CartController() {
    this.cartService = new CartService(new CartDAO(), new OrderDAO());
  }

  public void setCartService(CartService cartService) {
    this.cartService = cartService;
  }

  @FXML
  public void initialize() {
    setupTableColumns();
    loadCart();
  }

  private void setupTableColumns() {
    nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGame().getTitle()));
    priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getGame().getPrice()).asObject());
    quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
    totalColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(
            cellData.getValue().getQuantity() * cellData.getValue().getGame().getPrice()
    ).asObject());
  }

  private void loadCart() {
    var items = cartService.getCartItems(currentUserId);
    cartTable.getItems().setAll(items);
    updateTotal();
  }

  private void updateTotal() {
    double sum = cartService.calculateTotal(cartTable.getItems());
    totalLabel.setText(String.format("%.2f руб.", sum));
  }

  @FXML
  private void checkout() {
    int orderId = cartService.checkout(currentUserId);
    if (orderId > 0) {
      loadCart();
      showAlert("Готово", "Заказ #" + orderId + " успешно оформлен");
    } else {
      showAlert("Ошибка", "Не удалось оформить заказ или корзина пуста");
    }
  }

  @FXML
  private void clearCart() {
    cartService.clearCart(currentUserId);
    loadCart();
  }

  @FXML
  private void backToCatalog() {
    Stage stage = (Stage) cartTable.getScene().getWindow();
    stage.close();
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}

