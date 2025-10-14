package game_store.app.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO {

  DbConnection db = new DbConnection();

  public List<String> getAllGenres() {
    List<String> genres = new ArrayList<>();
    String sql = "SELECT DISTINCT name FROM game_genres ORDER BY name";

    try (Connection conn = db.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        genres.add(rs.getString("name"));
      }
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return genres;
  }

  public int getGenreIdByName(String name) {
    String sql = "SELECT genre_id FROM game_genres WHERE name = ?";
    try (Connection conn = db.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, name);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("genre_id");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return -1; // если не найден
  }
}
