package edu.uci.ics.zexis1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "items", required = true)
    ArrayList<OrderItemsModel> items;
    @JsonProperty(value = "redirectURL", required = true)
    String redirectURL;
    @JsonProperty(value = "token", required = true)
    String token;

    @JsonCreator
    public OrderResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                              @JsonProperty(value = "items", required = true) ArrayList<OrderItemsModel> items) {
        this.resultCode = resultCode;
        generalMessage();
        this.items = items;
    }
    @JsonCreator
    public OrderResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        generalMessage();
        this.items = null;
        this.redirectURL = null;
        this.token = null;
    }
    @JsonCreator
    public OrderResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                              @JsonProperty(value = "redirectURL", required = true) String redirectURL,
                              @JsonProperty(value = "token", required = true) String token) {
        this.resultCode = resultCode;
        generalMessage();
        this.items = null;
        this.redirectURL = redirectURL;
        this.token = token;
    }
    private void generalMessage(){
        switch (this.resultCode){
            case -3:
                this.message = "JSON Parse Exception.";
                break;
            case -2:
                this.message = "JSON Mapping Exception.";
                break;
            case -1:
                this.message = "Internal Server Error.";
                break;
            case 332:
                this.message = "Customer does not exist.";
                break;
            case 341:
                this.message = "Shopping cart for this customer not found.";
                break;
            case 342:
                this.message = "Create payment failed.";
                break;
            case 3410:
                this.message = "Orders retrieved successfully.";
                break;
            case 3400:
                this.message = "Order placed successfully.";
                break;
            case 3421:
                this.message = "Token not found.";
                break;
            case 3422:
                this.message = "Payment can not be completed.";
                break;
            case 3420:
                this.message = "Payment is completed successfully.";
                break;
        }
    }
    @JsonProperty("resultCode")
    public int getResultCode() {
        return resultCode;
    }
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
    @JsonProperty("items")
    public ArrayList<OrderItemsModel> getItems() {
        return items;
    }
    @JsonProperty("redirectURL")
    public String getRedirectURL() {
        return redirectURL;
    }
    @JsonProperty("token")
    public String getToken() {
        return token;
    }
}
