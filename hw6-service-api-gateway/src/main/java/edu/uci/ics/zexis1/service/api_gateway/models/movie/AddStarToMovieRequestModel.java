package edu.uci.ics.zexis1.service.api_gateway.models.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.RequestModel;

public class AddStarToMovieRequestModel extends RequestModel {
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
