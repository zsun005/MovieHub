package edu.uci.ics.zexis1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddStarRequestModel {
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
