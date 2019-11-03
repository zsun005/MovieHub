package edu.uci.ics.zexis1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.Model;
import edu.uci.ics.zexis1.service.api_gateway.utilities.ResultCodes;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponseModel extends Model {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "customer", required = true)
    CustomerRequestModel customer;

    @JsonCreator
    public CustomerResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                 @JsonProperty(value = "customer", required = true) CustomerRequestModel customer) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(this.resultCode);
        this.customer = customer;
    }
    @JsonCreator
    public CustomerResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
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
