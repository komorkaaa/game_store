package game_store.app.models;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

  private int id;
  private int userId;
  private double total;
  private LocalDateTime createdAt;
  private List<OrderItem> items;

  public Order() {

  }

  public Order(int id, int userId, double total, LocalDateTime createdAt) {
    this.id = id;
    this.userId = userId;
    this.total = total;
    this.createdAt = createdAt;
  }

  // геттеры
  public int getId() {
    return id;
  }
  public int getUserId() {
    return userId;
  }
  public double getTotal() {
    return total;
  }
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
  public List<OrderItem> getItems() {
    return items;
  }

  public void setItems(List<OrderItem> items) {
    this.items = items;
  }
}
