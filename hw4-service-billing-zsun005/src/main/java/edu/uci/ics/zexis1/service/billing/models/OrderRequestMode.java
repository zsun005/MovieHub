package edu.uci.ics.zexis1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestMode {
    @JsonProperty(value = "email", required = true)
    String email;

    public OrderRequestMode(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }


    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
}
