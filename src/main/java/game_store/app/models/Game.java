package game_store.app.models;

import java.sql.Date;

public class Game {

  private int id;
  private String title;
  private double price;
  private String genreName;
  private String description;
  private String imagePath;
  private Date releaseDate;
  private String developer;
  private String publisher;

  public Game() {
  }

  public Game(int id, String title, double price, String genreName, String description, Date releaseDate, String developer, String publisher) {
    this.id = id;
    this.title = title;
    this.price = price;
    this.genreName = genreName;
    this.description = description;
    this.releaseDate = releaseDate;
    this.developer = developer;
    this.publisher = publisher;
  }

  public int getId() { return id; }
  public String getTitle() { return title; }
  public double getPrice() { return price; }
  public String getGenreName() { return genreName; }
  public String getImagePath() { return imagePath; }
  public String getDescription() { return description; }
  public Date getReleaseDate() { return releaseDate; }
  public String getDeveloper() { return developer; }
  public String getPublisher() { return publisher; }

  public void setGenreName(String genreName) { this.genreName = genreName; }
  public void setId(int id) { this.id = id; }
  public void setTitle(String title) { this.title = title; }
  public void setPrice(double price) { this.price = price; }
  public void setDescription(String description) { this.description = description; }
  public void setImagePath(String imagePath) { this.imagePath = imagePath; }
  public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }
  public void setDeveloper(String developer) { this.developer = developer; }
  public void setPublisher(String publisher) { this.publisher = publisher; }

}
