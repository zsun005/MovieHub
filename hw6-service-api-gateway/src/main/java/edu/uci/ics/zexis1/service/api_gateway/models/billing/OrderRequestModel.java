package edu.uci.ics.zexis1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.RequestModel;

public class OrderRequestModel extends RequestModel {
    @JsonProperty(value = "email", required = true)
    String email;

    public OrderRequestModel(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }


    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
}
