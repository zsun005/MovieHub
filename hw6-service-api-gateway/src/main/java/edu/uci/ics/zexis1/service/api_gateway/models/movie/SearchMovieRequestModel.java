package edu.uci.ics.zexis1.service.api_gateway.models.movie;

import edu.uci.ics.zexis1.service.api_gateway.models.RequestModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchMovieRequestModel extends RequestModel {
    @JsonProperty(value = "title")
    String title;
    @JsonProperty(value = "genre")
    String genre;
    @JsonProperty(value = "year")
    int year;
    @JsonProperty(value = "director")
    String director;
    @JsonProperty(value = "hidden")
    Boolean hidden;
    @JsonProperty(value = "offset", required = true)
    int offset;
    @JsonProperty(value = "limit", required = true)
    int limit;
    @JsonProperty(value = "orderby", required = true)
    String orderby;
    @JsonProperty(value = "direction", required = true)
    String direction;

    @JsonCreator
    public SearchMovieRequestModel(@JsonProperty(value = "title") String title,
                                    @JsonProperty(value = "genre") String genre,
                                    @JsonProperty(value = "year") int year,
                                    @JsonProperty(value = "director") String director,
                                    @JsonProperty(value = "hidden") Boolean hidden,
                                    @JsonProperty(value = "offset", required = true) int offset,
                                    @JsonProperty(value = "limit", required = true) int limit,
                                    @JsonProperty(value = "direction", required = true) String direction,
                                    @JsonProperty(value = "orderby", required = true) String orderby) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        if(this.title == null)
            this.title = "%%";
        else this.title = "%" + this.title + "%";

        if(this.genre == null)
            this.genre = "%%";
        else this.genre = "%" + this.genre + "%";

        this.director = director;
        if(this.director == null)
            this.director = "%%";
        else this.director = "%" + this.director + "%";
        this.hidden = hidden;

        this.offset = offset;
        this.limit = limit;
        this.orderby = orderby;
        this.direction = direction;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }
    @JsonProperty("genre")
    public String getGenre() {
        return genre;
    }
    @JsonProperty("year")
    public int getYear() {
        return year;
    }
    @JsonProperty("director")
    public String getDirector() {
        return director;
    }
    @JsonProperty("offset")
    public int getOffset() {
        return offset;
    }
    @JsonProperty("limit")
    public int getLimit() {
        return limit;
    }
    @JsonProperty("hidden")
    public Boolean isHidden() {
        return hidden;
    }

    @JsonProperty("sortby")
    public String getDirection() {
        return direction;
    }
    @JsonProperty("orderby")
    public String getOrderby() {
        return orderby;
    }
}
