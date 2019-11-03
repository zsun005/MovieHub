package edu.uci.ics.zexis1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.movies.utilities.ResultCases;

import java.util.ArrayList;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "stars", required = true)
    ArrayList<StarModel> stars;

    @JsonCreator
    public StarResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                             @JsonProperty(value = "stars", required = true) ArrayList<StarModel> stars) {
        this.resultCode = resultCode;
        this.message = ResultCases.generalResultMessage(this.resultCode);
        this.stars = stars;
    }
    @JsonProperty("resultCode")
    public int getResultCode() {
        return resultCode;
    }
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
    @JsonProperty("stars")
    public ArrayList<StarModel> getStars() {
        return stars;
    }
}
