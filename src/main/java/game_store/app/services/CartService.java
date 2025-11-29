package game_store.app.services;

import game_store.app.dao.CartDAO;
import game_store.app.dao.OrderDAO;
import game_store.app.models.CartItem;
import game_store.app.models.Game;
import game_store.app.models.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartService {

  private final CartDAO cartDAO;
  private final OrderDAO orderDAO;

  public CartService(CartDAO cartDAO, OrderDAO orderDAO) {
    this.cartDAO = cartDAO;
    this.orderDAO = orderDAO;
  }

  public CartService() {
    this.cartDAO = new CartDAO();
    this.orderDAO = new OrderDAO();
  }

  // ← Добавлен новый метод
  public void addToCart(int userId, Game game) {
    cartDAO.addToCart(userId, game);
  }

  public List<CartItem> getCartItems(int userId) {
    return cartDAO.getCartItemsByUserId(userId);
  }

  public double calculateTotal(List<CartItem> items) {
    return items.stream()
            .mapToDouble(item -> item.getQuantity() * item.getGame().getPrice())
            .sum();
  }

  public int checkout(int userId) {
    List<CartItem> items = cartDAO.getCartItemsByUserId(userId);
    if (items.isEmpty()) return -1;

    List<OrderItem> orderItems = items.stream()
            .map(item -> new OrderItem(
                    0,
                    0,
                    item.getGame(),
                    item.getQuantity(),
                    item.getGame().getPrice()
            ))
            .collect(Collectors.toList());

    int orderId = orderDAO.createOrder(userId, orderItems);

    if (orderId > 0) {
      cartDAO.clearCart(userId);
    }

    return orderId;
  }

  public void clearCart(int userId) {
    cartDAO.clearCart(userId);
  }
}
