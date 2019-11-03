package edu.uci.ics.zexis1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddStarToMovieRequestModel {
    @JsonProperty(value = "starid", required = true)
    String starid;
    @JsonProperty(value = "movieid", required = true)
    String movieid;

    @JsonCreator
    public AddStarToMovieRequestModel(@JsonProperty(value = "starid", required = true) String starid,
                                      @JsonProperty(value = "movieid", required = true) String movieid) {
        this.starid = starid;
        this.movieid = movieid;
    }
    @JsonProperty("movieid")
    public String getMovieid() {
        return movieid;
    }
    @JsonProperty("starid")
    public String getStarid() {
        return starid;
    }
}
