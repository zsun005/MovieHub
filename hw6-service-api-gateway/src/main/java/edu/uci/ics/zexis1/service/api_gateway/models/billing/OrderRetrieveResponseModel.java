package edu.uci.ics.zexis1.service.api_gateway.models.billing;

import edu.uci.ics.zexis1.service.api_gateway.models.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.utilities.ResultCodes;


import java.util.ArrayList;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRetrieveResponseModel extends Model {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "transactions", required = true)
    ArrayList<TransactionModel> transactions;

    @JsonCreator
    public OrderRetrieveResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                         @JsonProperty(value = "transactions", required = true) ArrayList<TransactionModel> transactions) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(this.resultCode);
        this.transactions = transactions;
    }
    @JsonCreator
    public OrderRetrieveResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(this.resultCode);
        transactions = null;
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
