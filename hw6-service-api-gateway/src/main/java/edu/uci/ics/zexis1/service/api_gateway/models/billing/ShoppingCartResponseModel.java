package edu.uci.ics.zexis1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.Model;
import edu.uci.ics.zexis1.service.api_gateway.utilities.ResultCodes;

import java.util.ArrayList;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartResponseModel extends Model {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "items")
    ArrayList<ShoppingCartRequestModel> items;

    public ShoppingCartResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                     @JsonProperty(value = "items") ArrayList<ShoppingCartRequestModel> items) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.items = items;
    }

    @JsonProperty("resultCode")
    public int getResultCode() {
        return resultCode;
    }
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
    @JsonProperty("items")
    public ArrayList<ShoppingCartRequestModel> getItems() {
        return items;
    }
}
