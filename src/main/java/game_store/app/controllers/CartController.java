package game_store.app.controllers;

import game_store.app.dao.CartDAO;
import game_store.app.dao.OrderDAO;
import game_store.app.models.CartItem;
import game_store.app.models.OrderItem;
import game_store.app.models.Session;

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

    private CartDAO cartDAO = new CartDAO();
    private int currentUserId = Session.getCurrentUserId();

    @FXML
    public void initialize() {

        setupTableColumns();

        loadCart();
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getGame().getTitle()));
        priceColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getGame().getPrice()).asObject());
        quantityColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        totalColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(
                        cellData.getValue().getQuantity() * cellData.getValue().getGame().getPrice()
                ).asObject());
    }

    private void loadCart() {
        List<CartItem> items = cartDAO.getCartItemsByUserId(currentUserId);
        cartTable.getItems().setAll(items);
        updateTotal();
    }

    private void updateTotal() {
        double sum = cartTable.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getGame().getPrice())
                .sum();
        totalLabel.setText(String.format("%.2f руб.", sum));
    }

    @FXML
    private void checkout() {
        // Проверка на пустую корзину
        if (cartTable.getItems().isEmpty()) {
            showAlert("Корзина пуста", "Добавьте товары в корзину перед оформлением заказа.");
            return;
        }

        OrderDAO orderDAO = new OrderDAO();
        List<OrderItem> itemsForOrder = new ArrayList<>();

        // Подготовка товаров для заказа
        for (CartItem cartItem : cartTable.getItems()) {
            itemsForOrder.add(new OrderItem(0, 0, cartItem.getGame(),
                    cartItem.getQuantity(), cartItem.getGame().getPrice()));
        }

        // Создание заказа в базе данных
        int orderId = orderDAO.createOrder(Session.getCurrentUserId(), itemsForOrder);
        if (orderId > 0) {
            cartDAO.clearCart(Session.getCurrentUserId());
            loadCart(); // Обновление таблицы после успешного заказа
            showAlert("Готово", "Заказ #" + orderId + " успешно оформлен");
        } else {
            showAlert("Ошибка", "Не удалось оформить заказ");
        }
    }

    @FXML
    private void clearCart() {
        cartDAO.clearCart(currentUserId);
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
