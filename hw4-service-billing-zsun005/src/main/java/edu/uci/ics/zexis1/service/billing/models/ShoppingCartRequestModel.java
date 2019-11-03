package edu.uci.ics.zexis1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartRequestModel {
    @JsonProperty(value = "email", required = true)
    String email;
    @JsonProperty(value = "movieId")
    String movieId;
    @JsonProperty(value = "quantity")
    Integer quantity;

    @JsonCreator
    public ShoppingCartRequestModel(@JsonProperty(value = "email", required = true) String email,
                                    @JsonProperty(value = "movieId") String movieId,
                                    @JsonProperty(value = "quantity") Integer quantity) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
    }
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
    @JsonProperty("movieId")
    public String getMovieId() {
        return movieId;
    }
    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }
}