package game_store.app.services;

import game_store.app.dao.GameDAO;
import game_store.app.models.Game;

import java.sql.Date;
import java.util.List;

public class GameAdminService {

  private final GameDAO gameDAO = new GameDAO();

  public List<Game> getAllGames() {
    return gameDAO.getAllGames();
  }

  public void addGame(String title,
                      String genre,
                      double price,
                      String description,
                      Date releaseDate,
                      String developer,
                      String publisher) throws Exception {

    Game game = new Game(
            0,
            title,
            price,
            genre,
            description,
            releaseDate,
            developer,
            publisher
    );

    gameDAO.addGame(game);
  }

  public void updateGame(Game game,
                         String title,
                         String genre,
                         double price,
                         String description,
                         Date releaseDate,
                         String developer,
                         String publisher) throws Exception {

    game.setTitle(title);
    game.setGenreName(genre);
    game.setPrice(price);
    game.setDescription(description);
    game.setReleaseDate(releaseDate);
    game.setDeveloper(developer);
    game.setPublisher(publisher);

    gameDAO.updateGame(game);
  }

  public void deleteGame(int id) {
    gameDAO.deleteGame(id);
  }
}
