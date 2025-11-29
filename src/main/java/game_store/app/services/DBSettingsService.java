package game_store.app.services;

import java.util.prefs.Preferences;

public class DBSettingsService {

  private final Preferences prefs;

  public DBSettingsService() {
    this.prefs = Preferences.userRoot().node("pref");
  }

  public String getHost() {
    return prefs.get("host", "host");
  }

  public String getPort() {
    return prefs.get("port", "port");
  }

  public String getDbName() {
    return prefs.get("db_name", "db_name");
  }

  public String getLogin() {
    return prefs.get("login", "login");
  }

  public String getPassword() {
    return prefs.get("pass", "pass");
  }

  public void saveSettings(String host,
                           String port,
                           String dbName,
                           String login,
                           String pass) {

    prefs.put("host", host);
    prefs.put("port", port);
    prefs.put("db_name", dbName);
    prefs.put("login", login);
    prefs.put("pass", pass);
  }
}
