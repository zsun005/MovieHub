package edu.uci.ics.zexis1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.movies.utilities.ResultCases;

import java.util.ArrayList;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchMoviesResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "movies")
    ArrayList<MovieModel> movies;

    @JsonCreator
    public SearchMoviesResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                     @JsonProperty(value = "movies") ArrayList<MovieModel> movies) {
        this.resultCode = resultCode;
        this.message = ResultCases.generalResultMessage(resultCode);
        this.movies = movies;
    }
    @JsonProperty("resultCode")
    public int getResultCode() {
        return resultCode;
    }
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
    @JsonProperty("movies")
    public ArrayList<MovieModel> getMovies() {
        return movies;
    }
}
