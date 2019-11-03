package edu.uci.ics.zexis1.service.api_gateway.models.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.RequestModel;

public class AddGenreRequestModel extends RequestModel {
    @JsonProperty(value = "name", required = true)
    String name;

    @JsonCreator
    public AddGenreRequestModel( @JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }
    @JsonProperty("name")
    public String getName() {
        return name;
    }
}