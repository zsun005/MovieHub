package edu.uci.ics.zexis1.service.api_gateway.models;

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
    @JsonProperty(value = "sessionID")
    private String sessionID;

    @JsonCreator
    public VerifySessionResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                      @JsonProperty(value = "message", required = true) String message,
                                      @JsonProperty(value = "sessionID") String sessionID){
        this.resultCode = resultCode;
        this.message = message;
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
