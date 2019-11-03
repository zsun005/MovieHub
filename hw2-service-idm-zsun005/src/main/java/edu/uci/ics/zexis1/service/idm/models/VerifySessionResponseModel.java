package edu.uci.ics.zexis1.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "dataValid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifySessionResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "sessionID", required = true)
    private String sessionID;


    @JsonCreator
    public VerifySessionResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                      @JsonProperty(value = "sessionID", required = true) String sessionID)
    {
        this.resultCode = resultCode;
        this.sessionID = sessionID;

        switch(resultCode)
        {
            case -13:
                message = "Token has invalid length.";
                break;
            case -11:
                message = "Email address has invalid format.";
                break;
            case -10:
                message = "Email address has invalid length.";
                break;
            case -3:
                message = "JSON Parse Exception.";
                break;
            case -2:
                message = "JSON Mapping Exception.";
                break;
            case -1:
                message = "Internal server error.";
                break;
            case 14:
                message = "User not found.";
                break;
            case 130:
                message = "Session is active.";
                break;
            case 131:
                message = "Session is expired.";
                break;
            case 132:
                message = "Session is closed.";
                break;
            case 133:
                message = "Session is revoked.";
                break;
            case 134:
                message = "Session not found.";
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

    @JsonProperty("sessionID")
    public String getSessionID() {
        return sessionID;
    }

}
