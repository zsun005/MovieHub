package edu.uci.ics.zexis1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddRatingRequestModel {
    @JsonProperty(value = "id", required = true)
    String id;
    @JsonProperty(value = "rating", required = true)
    Float rating;

    @JsonCreator
    public AddRatingRequestModel(@JsonProperty(value = "id", required = true) String id,
                                 @JsonProperty(value = "rating", required = true) Float rating) {
        this.id = id;
        this.rating = rating;
    }
    @JsonProperty("id")
    public String getId() {
        return id;
    }
    @JsonProperty("rating")
    public Float getRating() {
        return rating;
    }
}
