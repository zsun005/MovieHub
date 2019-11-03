package edu.uci.ics.zexis1.service.api_gateway.models.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieModel {
    @JsonProperty(value = "movieId", required = true)
    String movieId;
    @JsonProperty(value = "title", required = true)
    String title;
    @JsonProperty(value = "director")
    String director;
    @JsonProperty(value = "year")
    Integer year;
    @JsonProperty(value = "backdrop_path")
    String backdrop_path;
    @JsonProperty(value = "budget")
    Integer budget;
    @JsonProperty(value = "overview")
    String overview;
    @JsonProperty(value = "poster_path")
    String poster_path;
    @JsonProperty(value = "revenue")
    Integer revenue;
    @JsonProperty(value = "rating")
    Float rating;
    @JsonProperty(value = "numVotes")
    Integer numVotes;
    @JsonProperty(value = "hidden")
    Boolean hidden;
    @JsonProperty(value = "genres")
    String genres;
    @JsonProperty(value = "stars")
    String stars;


    @JsonCreator
    public MovieModel(@JsonProperty(value = "movieId", required = true) String movieId,
                      @JsonProperty(value = "title", required = true) String title,
                      @JsonProperty(value = "director") String director,
                      @JsonProperty(value = "year") Integer year,
                      @JsonProperty(value = "rating") Float rating,
                      @JsonProperty(value = "numVotes") Integer numVotes,
                      @JsonProperty(value = "hidden") Boolean hidden)
    {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.year = year;
        this.rating = rating;
        this.numVotes = numVotes;
        this.hidden = hidden;
    }
    @JsonCreator
    public MovieModel(@JsonProperty(value = "movieId", required = true) String movieId,
                      @JsonProperty(value = "title", required = true) String title,
                      @JsonProperty(value = "director") String director,
                      @JsonProperty(value = "year") Integer year,
                      @JsonProperty(value = "backdrop_path") String backdrop_path,
                      @JsonProperty(value = "budget") Integer budget,
                      @JsonProperty(value = "overview") String overview,
                      @JsonProperty(value = "poster_path") String poster_path,
                      @JsonProperty(value = "revenue") Integer revenue,
                      @JsonProperty(value = "rating") Float rating,
                      @JsonProperty(value = "numVotes") Integer numVotes,
                      @JsonProperty(value = "genres") String genres,
                      @JsonProperty(value = "stars") String stars) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.year = year;
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        this.overview = overview;
        this.poster_path = poster_path;
        this.revenue = revenue;
        this.rating = rating;
        this.numVotes = numVotes;
        this.genres = genres;
        this.stars = stars;
    }
    @JsonProperty("movieId")
    public String getMovieId() {
        return movieId;
    }
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }
    @JsonProperty("director")
    public String getDirector() {
        return director;
    }
    @JsonProperty("year")
    public Integer getYear() {
        return year;
    }
    @JsonProperty("backdrop_path")
    public String getBackdrop_path() {
        return backdrop_path;
    }
    @JsonProperty("budget")
    public Integer getBudget() {
        return budget;
    }
    @JsonProperty("overview")
    public String getOverview() {
        return overview;
    }
    @JsonProperty("poster_path")
    public String getPoster_path() {
        return poster_path;
    }
    @JsonProperty("revnue")
    public Integer getRevenue() {
        return revenue;
    }
    @JsonProperty("rating")
    public Float getRating() {
        return rating;
    }
    @JsonProperty("numVotes")
    public Integer getNumVotes() {
        return numVotes;
    }
    @JsonProperty("hidden")
    public Boolean getHidden() {
        return hidden;
    }
    @JsonProperty("genres")
    public String getGenres() {
        return genres;
    }
    @JsonProperty("stars")
    public String getStars() {
        return stars;
    }

    @JsonCreator
    public void setHidden(@JsonProperty(value = "hidden") Boolean hidden){
        this.hidden = hidden;
    }
}
