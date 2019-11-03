package edu.uci.ics.zexis1.service.api_gateway.models.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.RequestModel;

public class AddRatingRequestModel extends RequestModel {
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
