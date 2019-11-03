package edu.uci.ics.zexis1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.RequestModel;

public class ShoppingCartRequestModel extends RequestModel {
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
