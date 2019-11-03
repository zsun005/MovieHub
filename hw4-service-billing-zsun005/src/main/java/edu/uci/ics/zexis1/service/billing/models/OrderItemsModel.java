package edu.uci.ics.zexis1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemsModel {
    @JsonProperty(value = "email", required = true)
    String email;
    @JsonProperty(value = "movieId", required = true)
    String movieId;
    @JsonProperty(value = "quantity", required = true)
    int quantity;
    @JsonProperty(value = "unit_price", required = true)
    float unit_price;
    @JsonProperty(value = "discount", required = true)
    float discount;
    @JsonProperty(value = "saleDate", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date saleDate;

    public OrderItemsModel(@JsonProperty(value = "email", required = true) String email,
                           @JsonProperty(value = "movieId", required = true) String movieId,
                           @JsonProperty(value = "quantity", required = true) int quantity,
                           @JsonProperty(value = "saleDate", required = true)
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date saleDate) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
        this.saleDate = saleDate;
    }
    public OrderItemsModel(@JsonProperty(value = "email", required = true) String email,
                           @JsonProperty(value = "movieId", required = true) String movieId,
                           @JsonProperty(value = "quantity", required = true) int quantity,
                           @JsonProperty(value = "unit_price", required = true) float unit_price,
                           @JsonProperty(value = "discount", required = true) float discount,
                           @JsonProperty(value = "saleDate", required = true)
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date saleDate) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.discount = discount;
        this.saleDate = saleDate;
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
    public int getQuantity() {
        return quantity;
    }
    @JsonProperty("saleDate")
    public Date getSaleDate() {
        return saleDate;
    }
    @JsonProperty("unit_price")
    public float getUnit_price() {
        return unit_price;
    }
    @JsonProperty("discount")
    public float getDiscount() {
        return discount;
    }
}
