package edu.uci.ics.zexis1.service.api_gateway.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VerifySessionRequestModel {
    @JsonProperty(value = "email", required = true)
    private String email;
    @JsonProperty(value = "sessionID", required = true)
    private String sessionID;

    @JsonCreator
    public VerifySessionRequestModel(@JsonProperty(value = "email", required = true) String email,
                                     @JsonProperty(value = "sessionID", required = true) String sessionID)
    {
        this.email = email;
        this.sessionID = sessionID;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
    @JsonCreator
    public void setEmail(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }


    @JsonProperty("sessionID")
    public String getSessionID() {
        return sessionID;
    }
    @JsonProperty
    public void setSessionID(@JsonProperty(value = "sessionID", required = true) String sessionID) {
        this.sessionID = sessionID;
    }
}
