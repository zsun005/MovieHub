package edu.uci.ics.zexis1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "customer", required = true)
    CustomerRequestModel customer;

    @JsonCreator
    public CustomerResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        generalMessageFromResultCode();
    }
    @JsonCreator
    public CustomerResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                 @JsonProperty(value = "customer", required = true) CustomerRequestModel customer) {
        this.resultCode = resultCode;
        generalMessageFromResultCode();
        this.customer = customer;
    }

    private void generalMessageFromResultCode(){
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
            case 321:
                this.message = "Credit card ID has invalid length.";
                break;
            case 322:
                this.message = "Credit card ID has invalid value.";
                break;
            case 331:
                this.message = "Credit card ID not found.";
                break;
            case 332:
                this.message = "Customer does not exist.";
                break;
            case 333:
                this.message = "Duplicate insertion.";
                break;
            case 3300:
                this.message = "Customer inserted successfully.";
                break;
            case 3310:
                this.message = "Customer updated successfully.";
                break;
            case 3320:
                this.message = "Customer retrieved successfully.";
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
    @JsonProperty("customer")
    public CustomerRequestModel getCustomer() {
        return customer;
    }
}
