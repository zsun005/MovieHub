package edu.uci.ics.zexis1.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IDMLoginResponseModel {
    private int resultCode;
    private String message;
    private String sessionID = null;

    @JsonCreator
    public IDMLoginResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode)
    {
        this.resultCode = resultCode;
        this.sessionID = sessionID;

        switch (resultCode)
        {
            case -12:
                message = "Password has invalid length (cannot be empty/null).";
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
            case 11:
                message = "Passwords do not match.";
                break;
            case 14:
                message = "User not found.";
                break;
            case 120:
                message = "User logged in successfully.";
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
    @JsonCreator
    public void setSessionID(@JsonProperty(value = "sessionID", required = true) String sessionID) {
        this.sessionID = sessionID;
    }
}
