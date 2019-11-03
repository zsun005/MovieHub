package edu.uci.ics.zexis1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddGenreReqeustModel {
    @JsonProperty(value = "name", required = true)
    String name;

    @JsonCreator
    public AddGenreReqeustModel( @JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }
    @JsonProperty("name")
    public String getName() {
        return name;
    }
}
