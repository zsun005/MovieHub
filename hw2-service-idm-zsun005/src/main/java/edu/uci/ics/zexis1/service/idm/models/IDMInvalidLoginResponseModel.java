package edu.uci.ics.zexis1.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IDMInvalidLoginResponseModel {
    private int resultCode;
    private String message;

    @JsonCreator
    public IDMInvalidLoginResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                        @JsonProperty(value = "message", required = true) String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    @JsonProperty("resultCode")
    public int getResultCode() {
        return resultCode;
    }

    @JsonCreator
    public void setResultCode(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
    @JsonCreator
    public void setMessage(@JsonProperty(value = "message", required = true) String message) {
        this.message = message;
    }
}
