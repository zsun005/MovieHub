package edu.uci.ics.zexis1.service.api_gateway.models.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.RequestModel;

public class AddStarRequestModel extends RequestModel {
    @JsonProperty(value = "name", required = true)
    String name;
    @JsonProperty(value = "birthYear")
    Integer birthYear;

    @JsonCreator
    public AddStarRequestModel(@JsonProperty(value = "name", required = true) String name,
                               @JsonProperty(value = "birthYear") Integer birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }
    @JsonProperty("name")
    public String getName() {
        return name;
    }
    @JsonProperty("birthYear")
    public Integer getBirthYear() {
        return birthYear;
    }
}
