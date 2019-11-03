package edu.uci.ics.zexis1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionModel {
    @JsonProperty(value = "transactionId", required = true)
    String transactionId;
    @JsonProperty(value = "state", required = true)
    String state;
    @JsonProperty(value = "amount", required = true)
    AmountModel amount;
    @JsonProperty(value = "transaction_fee", required = true)
    TransactionFeeModel transaction_fee;
    @JsonProperty(value = "create_time", required = true)
    String create_time;
    @JsonProperty(value = "update_time",required = true)
    String update_time;
    @JsonProperty(value = "items")
    ArrayList<OrderItemsModel> items;

    @JsonCreator
    public TransactionModel(@JsonProperty(value = "transactionId", required = true) String transactionId,
                            @JsonProperty(value = "state", required = true) String state,
                            @JsonProperty(value = "amount", required = true) AmountModel amount,
                            @JsonProperty(value = "transaction_fee", required = true) TransactionFeeModel transaction_fee,
                            @JsonProperty(value = "create_time", required = true) String create_time,
                            @JsonProperty(value = "update_time",required = true) String update_time,
                            @JsonProperty(value = "items") ArrayList<OrderItemsModel> items) {
        this.transactionId = transactionId;
        this.state = state;
        this.amount = amount;
        this.transaction_fee = transaction_fee;
        this.create_time = create_time;
        this.update_time = update_time;
        this.items = items;
    }
    @JsonProperty("transactionId")
    public String getTransactionId() {
        return transactionId;
    }
    @JsonProperty("state")
    public String getState() {
        return state;
    }
    @JsonProperty("amount")
    public AmountModel getAmount() {
        return amount;
    }
    @JsonProperty("transaction_fee")
    public TransactionFeeModel getTransaction_fee() {
        return transaction_fee;
    }
    @JsonProperty("create_time")
    public String getCreate_time() {
        return create_time;
    }
    @JsonProperty("update_time")
    public String getUpdate_time() {
        return update_time;
    }
    @JsonProperty("items")
    public ArrayList<OrderItemsModel> getItems() {
        return items;
    }
}
