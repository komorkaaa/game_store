package game_store.app.services;

import game_store.app.dao.OrderDAO;
import game_store.app.models.Order;
import game_store.app.models.OrderItem;

import java.util.List;

public class OrderService {

  private final OrderDAO orderDAO = new OrderDAO();

  public List<Order> getOrdersByUser(int userId) {
    return orderDAO.getOrdersByUserId(userId);
  }

  public List<OrderItem> getOrderItems(int orderId) {
    return orderDAO.getOrderItems(orderId);
  }
}
