package edu.uci.ics.zexis1.service.billing.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditCardIDModel {
    @JsonProperty(value = "id", required = true)
    String id;

    @JsonCreator
    public CreditCardIDModel(@JsonProperty(value = "id", required = true) String id) {
        this.id = id;
    }

    @JsonProperty(value = "id")
    public String getId() {
        return id;
    }
}
