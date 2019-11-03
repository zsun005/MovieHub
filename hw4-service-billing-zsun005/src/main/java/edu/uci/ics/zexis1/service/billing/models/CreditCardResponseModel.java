package edu.uci.ics.zexis1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCardResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "creditcard", required = true)
    CreditCardRequestModel creditcard;

    @JsonCreator
    public CreditCardResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        setMessage();
        this.creditcard = null;

    }
    public CreditCardResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                   @JsonProperty(value = "creditcard", required = true) CreditCardRequestModel creditcard) {
        this.resultCode = resultCode;
        setMessage();
        this.creditcard = creditcard;
    }
    private void setMessage(){
        switch (this.resultCode)
        {
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
            case 323:
                this.message = "expiration has invalid value.";
                break;

            case 325:
                this.message = "Duplicate insertion.";
                break;


            // Insert Case
            case 324:
                this.message = "Credit card does not exist.";
                break;
            case 3200:
                this.message = "Credit card inserted successfully.";
                break;

            case 3210:
                this.message = "Credit card updated successfully.";
                break;

            case 3220:
                this.message = "Credit card deleted successfully.";
                break;
            case 3230:
                this.message = "Credit card retrieved successfully.";
                break;


            default:
                resultCode = -1;
                message = "Internal Server Error.";
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
    @JsonProperty("creditcard")
    public CreditCardRequestModel getCreditcard() {
        return creditcard;
    }
}
