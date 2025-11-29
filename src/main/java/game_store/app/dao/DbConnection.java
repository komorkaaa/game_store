package game_store.app.dao;

import game_store.app.services.DBSettingsService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

  private final String HOST;
  private final String PORT;
  private final String DB_NAME;
  private final String LOGIN;
  private final String PASS;

  public DbConnection() {
    DBSettingsService settings = new DBSettingsService();
    this.HOST = settings.getHost();
    this.PORT = settings.getPort();
    this.DB_NAME = settings.getDbName();
    this.LOGIN = settings.getLogin();
    this.PASS = settings.getPassword();
  }

  public Connection getConnection() throws ClassNotFoundException, SQLException {
    String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?currentSchema=public";
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(connStr, LOGIN, PASS);
  }

  public boolean canConnect() {
    try (Connection conn = getConnection()) {
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
