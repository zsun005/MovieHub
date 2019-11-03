package edu.uci.ics.zexis1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.ArrayList;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRetrieve {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "transactions", required = true)
    ArrayList<TransactionModel> transactions;

    @JsonCreator
    public OrderRetrieve(@JsonProperty(value = "resultCode", required = true) int resultCode,
                         @JsonProperty(value = "transactions", required = true) ArrayList<TransactionModel> transactions) {
        this.resultCode = resultCode;
        generalMessage();
        this.transactions = transactions;
    }
    @JsonCreator
    public OrderRetrieve(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        generalMessage();
        transactions = null;
    }

    private void generalMessage(){
        switch (this.resultCode){
            case -3:
                this.message = "JSON Parse Exception.";
                break;
            case -2:
                this.message = "JSON Mapping Exception.";
                break;
            case -1:
                this.message = "Internal Server Error.";
                break;
            case 332:
                this.message = "Customer does not exist.";
                break;
            case 3410:
                this.message = "Orders retrieved successfully.";
                break;
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
    @JsonProperty("transactions")
    public ArrayList<TransactionModel> getTransactions() {
        return transactions;
    }
}
