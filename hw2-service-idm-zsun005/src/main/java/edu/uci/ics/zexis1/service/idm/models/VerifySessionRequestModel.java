package edu.uci.ics.zexis1.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.regex.Pattern;

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

    public boolean validEmailFormat()
    {
        //TODO: 1.<email>@<domain>.<domainextension>
        String re = "^[a-zA-Z0-9_+&*-]+(?:\\."  +
                "[a-zA-Z0-9_+&*-]+)*@"      +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(re);
        return pattern.matcher(email).matches();
    }

    public boolean validEmailLength()
    {
        return email.length() > 0 && email.length() <= 50;
    }

}
