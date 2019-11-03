package edu.uci.ics.zexis1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.RequestModel;

public class CustomerRetrieveRequestModel extends RequestModel {
    @JsonProperty(value = "email", required = true)
    String email;

    @JsonCreator
    public CustomerRetrieveRequestModel(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
}
