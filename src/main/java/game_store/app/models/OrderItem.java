package game_store.app.models;

public class OrderItem {

  private int id;
  private int orderId;
  private Game game;
  private int quantity;
  private double price;

  public  OrderItem() {

  }

  public OrderItem(int id, int orderId, Game game, int quantity, double price) {
    this.id = id;
    this.orderId = orderId;
    this.game = game;
    this.quantity = quantity;
    this.price = price;
  }

  public int getId() {
    return id;
  }
  public int getOrderId() {
    return orderId;
  }
  public Game getGame() {
    return game;
  }
  public int getQuantity() {
    return quantity;
  }
  public double getPrice() {
    return price;
  }
}
