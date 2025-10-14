package game_store.app.dao;

import game_store.app.models.Game;
import game_store.app.models.Order;
import game_store.app.models.OrderItem;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

  DbConnection db = new DbConnection();

  // Создать заказ: записать orders и order_items; возвращает id созданного заказа
  public int createOrder(int userId, List<OrderItem> items) {
    String insertOrderSql = "INSERT INTO orders(user_id, total) VALUES (?, ?) RETURNING order_id";
    String insertItemSql = "INSERT INTO order_items(order_id, game_id, quantity, price) VALUES (?, ?, ?, ?)";

    double total = items.stream().mapToDouble(i -> i.getQuantity() * i.getGame().getPrice()).sum();

    try (Connection conn = db.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSql)) {
        psOrder.setInt(1, userId);
        psOrder.setDouble(2, total);
        ResultSet rs = psOrder.executeQuery();
        if (!rs.next()) throw new SQLException("Can't insert order");
        int orderId = rs.getInt(1);

        try (PreparedStatement psItem = conn.prepareStatement(insertItemSql)) {
          for (OrderItem it : items) {
            psItem.setInt(1, orderId);
            psItem.setInt(2, it.getGame().getId());
            psItem.setInt(3, it.getQuantity());
            psItem.setDouble(4, it.getGame().getPrice());
            psItem.addBatch();
          }
          psItem.executeBatch();
        }
        conn.commit();
        return orderId;
      } catch (SQLException ex) {
        conn.rollback();
        throw ex;
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
      return -1;
    }
  }

  // Получить заказы пользователя (без позиций)
  public List<Order> getOrdersByUserId(int userId) {
    List<Order> orders = new ArrayList<>();
    String sql = "SELECT order_id, user_id, total, created_at FROM orders WHERE user_id = ? ORDER BY created_at DESC";
    try (Connection conn = db.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        orders.add(new Order(
                rs.getInt("order_id"),
                rs.getInt("user_id"),
                rs.getDouble("total"),
                rs.getTimestamp("created_at").toLocalDateTime()
        ));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return orders;
  }

  // Получить позиции конкретного заказа
  public List<OrderItem> getOrderItems(int orderId) {
    List<OrderItem> list = new ArrayList<>();
    String sql = "SELECT oi.order_item_id, oi.order_id, oi.game_id, oi.quantity, oi.price, g.description, g.title, release_date, developer, publisher, g.price AS current_price, gr.name AS genre_name " +
            "FROM order_items oi " +
            "JOIN games g ON oi.game_id = g.game_id " +
            "LEFT JOIN game_genres gr ON g.game_id = gr.game_id " +
            "WHERE oi.order_id = ?";

    try (Connection conn = db.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, orderId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        Game game = new Game(
                rs.getInt("game_id"),
                rs.getString("title"),
                rs.getDouble("price"),      // можно использовать oi.price вместо current_price, но модель Game хранит актуальную цену
                rs.getString("genre_name"),
                rs.getString("description"),
                rs.getDate("release_date"),
                rs.getString("developer"),
                rs.getString("publisher")
        );
        OrderItem oi = new OrderItem(
                rs.getInt("order_item_id"),
                rs.getInt("order_id"),
                game,
                rs.getInt("quantity"),
                rs.getDouble("price") // цена сохранена в позиции
        );
        list.add(oi);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return list;
  }
}

