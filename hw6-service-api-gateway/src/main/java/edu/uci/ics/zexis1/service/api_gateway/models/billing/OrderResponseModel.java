package edu.uci.ics.zexis1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.Model;
import edu.uci.ics.zexis1.service.api_gateway.utilities.ResultCodes;

import java.util.ArrayList;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseModel extends Model {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;
    @JsonProperty(value = "items", required = true)
    ArrayList<OrderItemsModel> items;
    @JsonProperty(value = "redirectURL", required = true)
    String redirectURL;
    @JsonProperty(value = "token", required = true)
    String token;

    @JsonCreator
    public OrderResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                              @JsonProperty(value = "items", required = true) ArrayList<OrderItemsModel> items) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.items = items;
    }
    @JsonCreator
    public OrderResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.items = null;
        this.redirectURL = null;
        this.token = null;
    }
    @JsonCreator
    public OrderResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                              @JsonProperty(value = "redirectURL", required = true) String redirectURL,
                              @JsonProperty(value = "token", required = true) String token) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.items = null;
        this.redirectURL = redirectURL;
        this.token = token;
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
    public ArrayList<OrderItemsModel> getItems() {
        return items;
    }
    @JsonProperty("redirectURL")
    public String getRedirectURL() {
        return redirectURL;
    }
    @JsonProperty("token")
    public String getToken() {
        return token;
    }
}
