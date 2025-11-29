package game_store.app.dao;

import game_store.app.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;

public class UserDAO {

  DbConnection db = new DbConnection();

  public boolean usernameExists(String username) {
    String sql = "SELECT 1 FROM users WHERE username = ?";
    try (Connection conn = db.getConnection();
         PreparedStatement st = conn.prepareStatement(sql)) {
      st.setString(1, username);
      ResultSet rs = st.executeQuery();
      return rs.next();
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
      return true;
    }
  }

  public boolean emailExists(String email) {
    String sql = "SELECT 1 FROM users WHERE email = ?";
    try (Connection conn = db.getConnection();
         PreparedStatement st = conn.prepareStatement(sql)) {
      st.setString(1, email);
      ResultSet rs = st.executeQuery();
      return rs.next();
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
      return true;
    }
  }

  public boolean registerUser(String username, String email, String plainPassword, LocalDate birthday, String inn, String pasport, String phone_number ) {
    if (usernameExists(username) || emailExists(email)) return false;
    String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    String sql = "INSERT INTO users (username, email, password_hash, birthday, inn, pasport, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = db.getConnection();
         PreparedStatement st = conn.prepareStatement(sql)) {
      st.setString(1, username);
      st.setString(2, email);
      st.setString(3, hashed);
      st.setDate(4, Date.valueOf(birthday));
      st.setString(5, inn);
      st.setString(6, pasport);
      st.setString(7, phone_number);
      st.executeUpdate();
      return true;
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
      return false;
    }
  }

  public User findByUsername(String username) {
    String sql = "SELECT * FROM users WHERE username = ?";
    try (Connection conn = db.getConnection();
         PreparedStatement st = conn.prepareStatement(sql)) {
      st.setString(1, username);
      ResultSet rs = st.executeQuery();
      if (rs.next()) {
        return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("role"),
                rs.getDate("birthday"),
                rs.getString("inn"),
                rs.getString("pasport"),
                rs.getString("phone_number")
        );
      }
    } catch (SQLException | ClassNotFoundException e) { e.printStackTrace(); }
    return null;
  }

  public User authenticate(String username, String plainPassword) throws  ClassNotFoundException, SQLException {
    User u = findByUsername(username);
    if (u == null) return null;
    if (BCrypt.checkpw(plainPassword, u.getPasswordHash())) return u;
    return null;
  }

}
