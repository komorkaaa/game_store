package game_store.app.services;

import game_store.app.dao.CartDAO;
import game_store.app.dao.OrderDAO;
import game_store.app.models.CartItem;
import game_store.app.models.Game;
import game_store.app.models.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

  private CartDAO cartDAOMock;
  private OrderDAO orderDAOMock;
  private CartService cartService;

  @BeforeEach
  void setUp() {
    cartDAOMock = Mockito.mock(CartDAO.class);
    orderDAOMock = Mockito.mock(OrderDAO.class);
    cartService = new CartService(cartDAOMock, orderDAOMock);
  }

  @Test
  void testGetCartItems() {
    int userId = 1;
    List<CartItem> mockItems = List.of(
            new CartItem(1, new Game(1, "Game1", 100.0), 2)
    );

    when(cartDAOMock.getCartItemsByUserId(userId)).thenReturn(mockItems);

    List<CartItem> items = cartService.getCartItems(userId);
    assertEquals(1, items.size());
    assertEquals("Game1", items.get(0).getGame().getTitle());
  }

  @Test
  void testCalculateTotal() {
    List<CartItem> items = List.of(
            new CartItem(1, new Game(1, "Game1", 100.0), 2),
            new CartItem(2, new Game(2, "Game2", 150.0), 3)
    );

    double total = cartService.calculateTotal(items);
    assertEquals(650.0, total);
  }

  @Test
  void testCheckoutSuccess() {
    int userId = 1;
    List<CartItem> items = List.of(
            new CartItem(1, new Game(1, "Game1", 100.0), 2)
    );

    when(cartDAOMock.getCartItemsByUserId(userId)).thenReturn(items);
    when(orderDAOMock.createOrder(eq(userId), anyList())).thenReturn(123);

    int orderId = cartService.checkout(userId);

    assertEquals(123, orderId);
    verify(cartDAOMock).clearCart(userId);
  }

  @Test
  void testCheckoutEmptyCart() {
    int userId = 1;

    when(cartDAOMock.getCartItemsByUserId(userId)).thenReturn(List.of());

    int orderId = cartService.checkout(userId);

    assertEquals(-1, orderId);
    verify(orderDAOMock, never()).createOrder(anyInt(), anyList());
  }

  @Test
  void testClearCart() {
    int userId = 1;
    cartService.clearCart(userId);
    verify(cartDAOMock).clearCart(userId);
  }
}
