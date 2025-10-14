package game_store.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
  private final String HOST = "localhost";
  private final String PORT = "5432";
  private final String DB_NAME = "game_store";
  private final String LOGIN = "postgres";
  private final String PASS = "postgres";

  private Connection dbConn = null;
  public Connection getConnection() throws ClassNotFoundException, SQLException {
    String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME+"?currentSchema=public";
    Class.forName("org.postgresql.Driver");

    dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
    return dbConn;
  }

}
