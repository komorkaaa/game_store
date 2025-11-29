package game_store.app.controllers;

import game_store.app.models.Order;
import game_store.app.models.OrderItem;
import game_store.app.models.Session;
import game_store.app.services.OrderService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrdersController {

  @FXML private TableView<Order> ordersTable;
  @FXML private TableColumn<Order, Number> idColumn;
  @FXML private TableColumn<Order, String> dateColumn;
  @FXML private TableColumn<Order, Number> totalColumn;

  private final OrderService orderService = new OrderService();

  @FXML
  public void initialize() {
    setupTableColumns();
    loadOrders();
  }

  private void setupTableColumns() {
    idColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()));
    dateColumn.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue()
                    .getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
    totalColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getTotal()));
  }

  private void loadOrders() {
    int userId = Session.getCurrentUserId();
    List<Order> orders = orderService.getOrdersByUser(userId);
    ordersTable.getItems().setAll(orders);
  }

  @FXML
  private void showSelectedOrderItems() {
    Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
    if (selectedOrder == null) {
      showAlert("Выберите заказ", "Выберите заказ в списке для просмотра деталей");
      return;
    }

    List<OrderItem> items = orderService.getOrderItems(selectedOrder.getId());
    StringBuilder orderDetails = new StringBuilder();
    for (OrderItem item : items) {
      orderDetails.append(item.getGame().getTitle())
              .append(" x").append(item.getQuantity())
              .append(" — ").append(item.getPrice())
              .append(" руб.\n");
    }

    showAlert("Позиции заказа #" + selectedOrder.getId(), orderDetails.toString());
  }

  @FXML
  private void close() {
    Stage stage = (Stage) ordersTable.getScene().getWindow();
    stage.close();
  }

  private void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.getDialogPane().setPrefWidth(400);
    alert.showAndWait();
  }
}
