package game_store.app.models;

public class CartItem {

    private int id;
    private Game game;
    private int quantity;

    public CartItem() {
    }

    public CartItem(int id, Game game, int quantity) {
      this.id = id;
      this.game = game;
      this.quantity = quantity;
    }

    public int getId() { return id; }
    public Game getGame() { return game; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return game != null ? game.getPrice() : 0.0; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setGame(Game game) {  this.game = game; }

}
