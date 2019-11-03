package edu.uci.ics.zexis1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmountModel {
    @JsonProperty(value = "total", required = true)
    String total;
    @JsonProperty(value = "currency", required = true)
    String currency;

    @JsonCreator
    public AmountModel(@JsonProperty(value = "total", required = true) String total,
                       @JsonProperty(value = "currency", required = true) String currency) {
        this.total = total;
        this.currency = currency;
    }
    @JsonProperty("total")
    public String getTotal() {
        return total;
    }
    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }
}
