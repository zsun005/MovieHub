package edu.uci.ics.zexis1.service.api_gateway.models.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.RequestModel;

import java.util.ArrayList;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddMovieRequestModel extends RequestModel {
    @JsonProperty(value = "title", required = true)
    String title;
    @JsonProperty(value = "director", required = true)
    String director;
    @JsonProperty(value = "year", required = true)
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
    @JsonProperty(value = "genres", required = true)
    ArrayList<GenreModel> genres;

    @JsonCreator
    public AddMovieRequestModel(@JsonProperty(value = "title", required = true) String title,
                                @JsonProperty(value = "director", required = true) String director,
                                @JsonProperty(value = "year", required = true) int year,
                                @JsonProperty(value = "backdrop_path") String backdrop_path,
                                @JsonProperty(value = "budget") Integer budget,
                                @JsonProperty(value = "overview") String overview,
                                @JsonProperty(value = "poster_path") String poster_path,
                                @JsonProperty(value = "revenue") Integer revenue,
                                @JsonProperty(value = "genres", required = true) ArrayList<GenreModel> genres) {
        this.title = title;
        this.director = director;
        this.year = year;
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        this.overview = overview;
        this.poster_path = poster_path;
        this.revenue = revenue;
        this.genres = genres;
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
    public int getYear() {
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
    @JsonProperty("revenue")
    public Integer getRevenue() {
        return revenue;
    }
    @JsonProperty("genres")
    public ArrayList<GenreModel> getGenres() {
        return genres;
    }
}
