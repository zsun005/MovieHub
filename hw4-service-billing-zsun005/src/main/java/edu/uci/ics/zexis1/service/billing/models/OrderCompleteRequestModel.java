package edu.uci.ics.zexis1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderCompleteRequestModel {
    @JsonProperty(value = "paymentId", required = true)
    String paymentId;
    @JsonProperty(value = "token", required = true)
    String token;
    @JsonProperty(value = "PayerID", required = true)
    String PayerID;

    @JsonCreator
    public OrderCompleteRequestModel(@JsonProperty(value = "paymentId", required = true) String paymentId,
                                     @JsonProperty(value = "token", required = true) String token,
                                     @JsonProperty(value = "PayerID", required = true) String payerID) {
        this.paymentId = paymentId;
        this.token = token;
        PayerID = payerID;
    }
    @JsonProperty("paymentId")
    public String getPaymentId() {
        return paymentId;
    }
    @JsonProperty("token")
    public String getToken() {
        return token;
    }
    @JsonProperty("PayerID")
    public String getPayerID() {
        return PayerID;
    }
}
