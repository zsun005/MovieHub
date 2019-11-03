package edu.uci.ics.zexis1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchStarRequestModel {
    @JsonProperty(value = "name")
    String name;
    @JsonProperty(value = "birthYear")
    Integer birthYear;
    @JsonProperty(value = "movieTitle")
    String movieTitle;
    @JsonProperty(value = "limit")
    int limit;
    @JsonProperty(value = "offset")
    int offset;
    @JsonProperty(value = "orderby")
    String orderby;
    @JsonProperty(value = "direction")
    String direction;

    @JsonCreator

    public SearchStarRequestModel(@Context HttpHeaders headers, @JsonProperty(value = "name") String name,
                                  @JsonProperty(value = "birthYear") Integer birthYear,
                                  @JsonProperty(value = "movieTitle") String movieTitle,
                                  @JsonProperty(value = "limit") int limit,
                                  @JsonProperty(value = "offset") int offset,
                                  @JsonProperty(value = "orderby") String orderby,
                                  @JsonProperty(value = "direction") String direction) {
        this.name = name;
        this.birthYear = birthYear;
        this.movieTitle = movieTitle;
        this.limit = limit;
        this.offset = offset;
        this.orderby = orderby;
        this.direction = direction;
    }
    @JsonProperty("name")
    public String getName() {
        return name;
    }
    @JsonProperty("birthYear")
    public Integer getBirthYear() {
        return birthYear;
    }
    @JsonProperty("movieTitle")
    public String getMovieTitle() {
        return movieTitle;
    }
    @JsonProperty("limit")
    public int getLimit() {
        return limit;
    }
    @JsonProperty("offset")
    public int getOffset() {
        return offset;
    }
    @JsonProperty("orderby")
    public String getOrderby() {
        return orderby;
    }
    @JsonProperty("direction")
    public String getDirection() {
        return direction;
    }
}
