package game_store.app.services;

import game_store.app.dao.GameDAO;
import game_store.app.dao.GenreDAO;
import game_store.app.models.Game;

import java.util.List;

public class GameCatalogService {

  private final GameDAO gameDAO = new GameDAO();
  private final GenreDAO genreDAO = new GenreDAO();

  public List<Game> getAllGames() {
    return gameDAO.getAllGames();
  }

  public List<String> getAllGenres() {
    return genreDAO.getAllGenres();
  }

  public List<Game> getGamesByGenre(String genreName) {
    int genreId = genreDAO.getGenreIdByName(genreName);
    return gameDAO.getGamesByGenre(genreId);
  }

  public List<Game> searchGames(String keyword) {
    return gameDAO.searchGamesByName(keyword);
  }
}
