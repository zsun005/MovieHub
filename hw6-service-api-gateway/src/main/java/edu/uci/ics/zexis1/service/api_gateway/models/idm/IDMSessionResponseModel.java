package edu.uci.ics.zexis1.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.Model;
import edu.uci.ics.zexis1.service.api_gateway.utilities.ResultCodes;


public class IDMSessionResponseModel extends Model {
    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "sessionID", required = true)
    private String sessionID;


    @JsonCreator
    public IDMSessionResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                      @JsonProperty(value = "sessionID", required = true) String sessionID)
    {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
        this.sessionID = sessionID;
    }
    @JsonProperty("resultCode")
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("sessionID")
    public String getSessionID() {
        return sessionID;
    }

}
