package game_store.app.dao;

import game_store.app.models.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class GameDAO {

  DbConnection db = new DbConnection();

  public List<Game> getAllGames() {
    List<Game> games = new ArrayList<>();
    String sql = "SELECT g.game_id, g.title, g.price,  g.description, g.release_date, developer, publisher,  gg.name as genre_name   " +
            "FROM games g " +
            "JOIN game_genres gg ON g.game_id = gg.game_id";

    try (Connection conn = db.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {


      while (rs.next()) {
        games.add(new Game(
                rs.getInt("game_id"),
                rs.getString("title"),
                rs.getDouble("price"),
                rs.getString("genre_name"),
                rs.getString("description"),
                rs.getDate("release_date"),
                rs.getString("developer"),
                rs.getString("publisher")
        ));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return games;
  }

  public List<Game> getGamesByGenre(int genreId) {
    List<Game> games = new ArrayList<>();
    String sql = "SELECT g.game_id, g.title, g.description, g.price, g.release_date, developer, publisher, gg.name as genre_name " +
            "FROM games g " +
            "JOIN game_genres gg ON g.game_id = gg.game_id " +
            "WHERE gg.genre_id = ?";

    try (Connection conn = db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, genreId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        games.add(new Game(
                rs.getInt("game_id"),
                rs.getString("title"),
                rs.getDouble("price"),
                rs.getString("genre_name"),
                rs.getString("description"),
                rs.getDate("release_date"),
                rs.getString("developer"),
                rs.getString("publisher")
        ));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return games;
  }

  public List<Game> searchGamesByName(String keyword) {
    List<Game> games = new ArrayList<>();
    String sql = "SELECT g.game_id, g.title, g.description, g.price, g.release_date, developer, publisher, gg.name as genre_name " +
            "FROM games g " +
            "JOIN game_genres gg ON g.game_id = gg.game_id " +
            "WHERE title ILIKE ?";

    try (Connection conn = db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, "%" + keyword + "%");
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        games.add(new Game(
                rs.getInt("game_id"),
                rs.getString("title"),
                rs.getDouble("price"),
                rs.getString("genre_name"),
                rs.getString("description"),
                rs.getDate("release_date"),
                rs.getString("developer"),
                rs.getString("publisher")
        ));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return games;
  }

  public void addGame(Game game) {
    String sqlGame = "INSERT INTO games (title, price, description, release_date, developer, publisher) VALUES (?, ?, ?, ?, ?, ?) RETURNING game_id";
    String sqlGenre = "INSERT INTO game_genres (game_id, genre_id, name) VALUES (?, ?, ?)";

    try (Connection conn = db.getConnection();
         PreparedStatement stmtGame = conn.prepareStatement(sqlGame)) {

      // Вставляем основную информацию об игре
      stmtGame.setString(1, game.getTitle());
      stmtGame.setDouble(2, game.getPrice());
      stmtGame.setString(3, game.getDescription());
      stmtGame.setDate(4, game.getReleaseDate());
      stmtGame.setString(5, game.getDeveloper());
      stmtGame.setString(6, game.getPublisher());

      ResultSet rs = stmtGame.executeQuery();
      if (rs.next()) {
        int gameId = rs.getInt("game_id");

        // Получаем или создаем genre_id и вставляем в game_genres
        int genreId = getOrCreateGenreId(game.getGenreName(), conn);

        try (PreparedStatement stmtGenre = conn.prepareStatement(sqlGenre)) {
          stmtGenre.setInt(1, gameId);
          stmtGenre.setInt(2, genreId);
          stmtGenre.setString(3, game.getGenreName());
          stmtGenre.executeUpdate();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void updateGame(Game game) {
    String sqlGame = "UPDATE games SET title = ?, price = ?, description = ?, release_date = ?, developer = ?, publisher = ? WHERE game_id = ?";
    String sqlGenre = "UPDATE game_genres SET genre_id = ?, name = ? WHERE game_id = ?";

    try (Connection conn = db.getConnection();
         PreparedStatement stmtGame = conn.prepareStatement(sqlGame);
         PreparedStatement stmtGenre = conn.prepareStatement(sqlGenre)) {

      // Обновляем основную информацию об игре
      stmtGame.setString(1, game.getTitle());
      stmtGame.setDouble(2, game.getPrice());
      stmtGame.setString(3, game.getDescription());
      stmtGame.setDate(4, game.getReleaseDate());
      stmtGame.setString(5, game.getDeveloper());
      stmtGame.setString(6, game.getPublisher());
      stmtGame.setInt(7, game.getId());
      stmtGame.executeUpdate();

      // Получаем или создаем genre_id и обновляем game_genres
      int genreId = getOrCreateGenreId(game.getGenreName(), conn);
      stmtGenre.setInt(1, genreId);
      stmtGenre.setString(2, game.getGenreName());
      stmtGenre.setInt(3, game.getId());
      stmtGenre.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteGame(int gameId) {
    String sqlOrderItems = "DELETE FROM order_items WHERE game_id = ?";
    String sqlGameGenres = "DELETE FROM game_genres WHERE game_id = ?";
    String sqlGame = "DELETE FROM games WHERE game_id = ?";

    try (Connection conn = db.getConnection()) {
      conn.setAutoCommit(false); // Начинаем транзакцию

      try {
        // 1. Сначала удаляем из order_items
        try (PreparedStatement stmtOrderItems = conn.prepareStatement(sqlOrderItems)) {
          stmtOrderItems.setInt(1, gameId);
          stmtOrderItems.executeUpdate();
        }

        // 2. Затем удаляем из game_genres
        try (PreparedStatement stmtGameGenres = conn.prepareStatement(sqlGameGenres)) {
          stmtGameGenres.setInt(1, gameId);
          stmtGameGenres.executeUpdate();
        }

        // 3. И только потом удаляем саму игру
        try (PreparedStatement stmtGame = conn.prepareStatement(sqlGame)) {
          stmtGame.setInt(1, gameId);
          stmtGame.executeUpdate();
        }

        conn.commit(); // Подтверждаем транзакцию

      } catch (SQLException e) {
        conn.rollback(); // Откатываем в случае ошибки
        throw e;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  // Метод для получения ID жанра по названию (создает новый, если не существует)
  private int getOrCreateGenreId(String genreName, Connection conn) throws SQLException {
    // Сначала пытаемся найти существующий жанр
    String findSql = "SELECT genre_id FROM genres WHERE name = ?";
    try (PreparedStatement stmt = conn.prepareStatement(findSql)) {
      stmt.setString(1, genreName);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("genre_id");
      }
    }

    // Если жанр не найден - создаем новый
    // Сначала находим максимальный ID
    String maxIdSql = "SELECT COALESCE(MAX(genre_id), 0) + 1 as next_id FROM genres";
    int nextId;
    try (PreparedStatement stmt = conn.prepareStatement(maxIdSql);
         ResultSet rs = stmt.executeQuery()) {
      if (rs.next()) {
        nextId = rs.getInt("next_id");
      } else {
        throw new SQLException("Could not determine next genre_id");
      }
    }

    // Вставляем новый жанр
    String insertSql = "INSERT INTO genres (genre_id, name) VALUES (?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
      stmt.setInt(1, nextId);
      stmt.setString(2, genreName);
      stmt.executeUpdate();
      return nextId;
    }
  }
}

