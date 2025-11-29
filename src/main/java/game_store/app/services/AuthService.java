package game_store.app.services;

import game_store.app.dao.DbConnection;
import game_store.app.dao.UserDAO;
import game_store.app.models.User;

import java.sql.SQLException;

public class AuthService {

  private final UserDAO userDAO = new UserDAO();
  private final DbConnection dbConnection = new DbConnection();

  public boolean canConnect() {
    return dbConnection.canConnect();
  }

  public User login(String username, String password) throws SQLException, ClassNotFoundException {
    return userDAO.authenticate(username, password);
  }
}
