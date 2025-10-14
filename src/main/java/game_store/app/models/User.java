package game_store.app.models;

public class User {
  private int userId;
  private String username;
  private String email;
  private String passwordHash;
  private String role;

  public User() {

  }

  public User(int userId, String username, String email, String passwordHash, String role) {
    this.userId = userId;
    this.username = username;
    this.email = email;
    this.passwordHash = passwordHash;
    this.role = role;
  }

  public int getUserId() {
    return userId;
  }
  public String getUsername() {
    return username;
  }
  public String getEmail() {
    return email;
  }
  public String getPasswordHash() {
    return passwordHash;
  }
  public String getRole() {
    return role;
  }
}
