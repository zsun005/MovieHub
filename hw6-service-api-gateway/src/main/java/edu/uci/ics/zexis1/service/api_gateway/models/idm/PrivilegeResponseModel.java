package edu.uci.ics.zexis1.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.Model;
import edu.uci.ics.zexis1.service.api_gateway.utilities.ResultCodes;


public class PrivilegeResponseModel extends Model {
    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;
    @JsonProperty(value = "message", required = true)
    private String message;

    public PrivilegeResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
    }

    @JsonProperty("resultCode")
    public int getResultCode() {
        return resultCode;
    }
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}
