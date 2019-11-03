package edu.uci.ics.zexis1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenreModel {
    @JsonProperty(value = "id")
    int id;
    @JsonProperty(value = "name")
    String name;

    public GenreModel(@JsonProperty(value = "id") int id,
                      @JsonProperty(value = "name") String name) {
        this.id = id;
        this.name = name;
    }
    @JsonProperty("id")
    public int getId() {
        return id;
    }
    @JsonProperty("name")
    public String getName() {
        return name;
    }
}
