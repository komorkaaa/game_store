package game_store.app.controllers;

import game_store.app.dao.OrderDAO;
import game_store.app.models.Order;
import game_store.app.models.OrderItem;
import game_store.app.models.Session;
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

  // Элементы интерфейса истории заказов
  @FXML private TableView<Order> ordersTable;
  @FXML private TableColumn<Order, Number> idColumn;
  @FXML private TableColumn<Order, String> dateColumn;
  @FXML private TableColumn<Order, Number> totalColumn;

  private OrderDAO orderDAO = new OrderDAO();

  @FXML
  public void initialize() {

    setupTableColumns();

    loadOrders();
  }

  private void setupTableColumns() {
    idColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()));
    dateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
    totalColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getTotal()));
  }


  private void loadOrders() {
    int userId = Session.getCurrentUserId();
    List<Order> orders = orderDAO.getOrdersByUserId(userId);
    ordersTable.getItems().setAll(orders);
  }

  @FXML
  private void showSelectedOrderItems() {
    Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
    if (selectedOrder == null) {
      showAlert("Выберите заказ", "Выберите заказ в списке для просмотра деталей");
      return;
    }

    List<OrderItem> items = orderDAO.getOrderItems(selectedOrder.getId());
    StringBuilder orderDetails = new StringBuilder();

    // Формирование детальной информации о заказе
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
    Stage st = (Stage) ordersTable.getScene().getWindow();
    st.close();
  }

  private void showAlert(String title, String text) {
    Alert a = new Alert(Alert.AlertType.INFORMATION);
    a.setTitle(title);
    a.setHeaderText(null);
    a.setContentText(text);
    a.getDialogPane().setPrefWidth(400);
    a.showAndWait();
  }
}
