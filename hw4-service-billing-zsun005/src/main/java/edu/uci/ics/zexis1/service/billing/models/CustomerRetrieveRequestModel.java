package edu.uci.ics.zexis1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerRetrieveRequestModel {
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
