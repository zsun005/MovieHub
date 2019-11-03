package edu.uci.ics.zexis1.service.billing.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "items")
    ArrayList<ShoppingCartRequestModel> items;

    @JsonCreator
    public ShoppingCartResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                     @JsonProperty(value = "items", required = true) ArrayList<ShoppingCartRequestModel> items) {
        this.resultCode = resultCode;
        generalMessageFromResultCode();
        this.items = items;
    }
    @JsonCreator
    public ShoppingCartResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        generalMessageFromResultCode();
        items = null;
    }

    private void generalMessageFromResultCode(){
        switch (this.resultCode)
        {

            // General Cases
            case -11:
                this.message = "Email address has invalid format.";
                break;
            case -10:
                this.message = "Email address has invalid length.";
                break;
            case -3:
                this.message = "JSON Parse Exception.";
                break;
            case -2:
                this.message = "JSON Mapping Exception.";
                break;
            case -1:
                this.message = "Internal Server Error.";
                break;
            case 33:
                this.message = "Quantity has invalid value.";
                break;

            // insert Cases
            case 311:
                this.message = "Duplicate insertion.";
                break;
            case 3100:
                this.message = "Shopping cart item inserted successfully.";
                break;

            // update cases

            case 312:
                this.message = "Shopping item does not exist.";
                break;
            case 3110:
                this.message = "Shopping cart item updated successfully.";
                break;

            // delete cases
            case 3120:
                this.message = "Shopping cart item deleted successfully.";
                break;

            // retrieve cart

            case 3130:
                this.message = "Shopping cart retrieved successfully.";
                break;
            // clear cart

            case 3140:
                this.message = "Shopping cart cleared successfully.";
                break;

            default:
                this.message = "ShoppingCartResponseModel DID NOT CATCH RESULTCODE.";
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
    public ArrayList<ShoppingCartRequestModel> getItems() {
        return items;
    }
}
