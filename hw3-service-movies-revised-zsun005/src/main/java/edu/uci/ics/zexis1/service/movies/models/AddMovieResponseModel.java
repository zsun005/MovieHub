package edu.uci.ics.zexis1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.movies.utilities.ResultCases;

import java.util.ArrayList;

public class AddMovieResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "movieid", required = true)
    String movieid;
    @JsonProperty(value = "genreid", required = true)
    ArrayList<Integer> genreid;

    @JsonCreator
    public AddMovieResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                 @JsonProperty(value = "movieid", required = true) String movieid,
                                 @JsonProperty(value = "genreid", required = true) ArrayList<Integer> genreid) {
        this.resultCode = resultCode;
        this.message = ResultCases.generalResultMessage(resultCode);
        this.movieid = movieid;
        this.genreid = genreid;
    }
    @JsonProperty("resultCode")
    public int getResultCode() {
        return resultCode;
    }
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
    @JsonProperty("movieid")
    public String getMovieid() {
        return movieid;
    }
    @JsonProperty("genreid")
    public ArrayList<Integer> getGenreid() {
        return genreid;
    }
}
