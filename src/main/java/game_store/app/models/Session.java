package game_store.app.models;

public class Session {
  private static User currentUser;

  public static void setCurrentUser(User user) {
    currentUser = user;
  }

  public static User getCurrentUser() {
    return currentUser;
  }

  public static int getCurrentUserId() {
    return currentUser != null ? currentUser.getUserId() : -1;
  }

  public static void clear() {
    currentUser = null;
  }

  public static boolean isAdmin() {
    return currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole());
  }

  public static String getCurrentUserRole() {
    return currentUser != null ? currentUser.getRole() : "guest";
  }
}
