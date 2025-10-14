package game_store.app.dao;

import game_store.app.models.CartItem;
import game_store.app.models.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

  DbConnection db = new DbConnection();

  // Добавление игры в корзину
  public void addToCart(int userId, Game game) {
    String checkSql = "SELECT quantity FROM cart_items WHERE user_id=? AND game_id=?";
    String insertSql = "INSERT INTO cart_items(user_id, game_id, quantity) VALUES(?, ?, 1)";
    String updateSql = "UPDATE cart_items SET quantity = quantity + 1 WHERE user_id=? AND game_id=?";

    try (Connection conn = db.getConnection()) {
      PreparedStatement checkStmt = conn.prepareStatement(checkSql);
      checkStmt.setInt(1, userId);
      checkStmt.setInt(2, game.getId());
      ResultSet rs = checkStmt.executeQuery();

      if (rs.next()) {
        // если игра уже есть → увеличиваем количество
        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
        updateStmt.setInt(1, userId);
        updateStmt.setInt(2, game.getId());
        updateStmt.executeUpdate();
      } else {
        // если нет → создаём новую строку
        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
        insertStmt.setInt(1, userId);
        insertStmt.setInt(2, game.getId());
        insertStmt.executeUpdate();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public List<CartItem> getCartItemsByUserId(int userId) {
    List<CartItem> items = new ArrayList<>();
    String sql = "SELECT g.game_id, g.title, g.price, ci.quantity " +
            "FROM cart_items ci " +
            "JOIN games g ON ci.game_id = g.game_id " +
            "WHERE ci.user_id = ?";

    try (Connection conn = db.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, userId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        Game game = new Game();

        game.setId(rs.getInt("game_id"));
        game.setTitle(rs.getString("title"));
        game.setPrice(rs.getDouble("price"));

        CartItem item = new CartItem();
        item.setGame(game);
        item.setQuantity(rs.getInt("quantity"));
        items.add(item);

      }
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return items;
  }

  public void removeFromCart(int userId, int gameId) {
    String sql = "DELETE FROM cart_items WHERE user_id = ? AND game_id = ?";
    try (Connection conn = db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, userId);
      stmt.setInt(2, gameId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  // Очистить корзину пользователя
  public void clearCart(int userId) {
    String sql = "DELETE FROM cart_items WHERE user_id = ?";
    try (Connection conn = db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, userId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
