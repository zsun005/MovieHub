package edu.uci.ics.zexis1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionFeeModel {
    @JsonProperty(value = "value", required = true)
    String value;
    @JsonProperty(value = "currency", required = true)
    String currency;

    @JsonCreator
    public TransactionFeeModel(@JsonProperty(value = "value", required = true) String value,
                               @JsonProperty(value = "currency", required = true)String currency) {
        this.value = value;
        this.currency = currency;
    }
    @JsonProperty("value")
    public String getValue() {
        return value;
    }
    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }
}