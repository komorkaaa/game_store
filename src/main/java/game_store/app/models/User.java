package game_store.app.models;

import java.util.Date;

public class User {
  private int userId;
  private String username;
  private String email;
  private String passwordHash;
  private String role;
  private Date birthday;
  private String inn;
  private String pasport;
  private String phone_number;

  public User() {

  }

  public User(int userId, String username, String email, String passwordHash, String role, Date birthday, String inn, String pasport, String phone_number) {
    this.userId = userId;
    this.username = username;
    this.email = email;
    this.passwordHash = passwordHash;
    this.role = role;
    this.birthday = birthday;
    this.inn = inn;
    this.pasport = pasport;
    this.phone_number = phone_number;
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
