package edu.uci.ics.zexis1.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IDMRegisterResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;

    @JsonCreator
    public IDMRegisterResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        switch (resultCode)
        {
            case -12:
                message = "Password has invalid length (cannot be empty/null).";
                break;
            case -11:
                message = " Email address has invalid format.";
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
                message = "Internal Server Error.";
                break;
            case 12:
                message = "Password does not meet length requirements.";
                break;
            case 13:
                message = "Password does not meet character requirements.";
                break;
            case 16:
                message = "Email already in use.";
                break;
            case 110:
                message = "User registered successfully.";
                break;
        }
    }
    @JsonProperty("resultCode")
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty("message")
    public String getMessage(){ return message; }
}
