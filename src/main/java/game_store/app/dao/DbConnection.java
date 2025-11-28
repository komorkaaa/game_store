package game_store.app.dao;

import game_store.app.controllers.DBSettingsController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {


  DBSettingsController data = new DBSettingsController();

  private final String HOST = data.userData.get("host", "host");
  private final String PORT = data.userData.get("port", "port");
  private final String DB_NAME = data.userData.get("db_name", "db_name");
  private final String LOGIN = data.userData.get("login", "login");
  private final String PASS = data.userData.get("pass", "pass");

  // localhost:5432 game_store postgres

  private Connection dbConn = null;
  public Connection getConnection() throws ClassNotFoundException, SQLException {
    String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME+"?currentSchema=public";
    Class.forName("org.postgresql.Driver");

    dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
    return dbConn;
  }

}
