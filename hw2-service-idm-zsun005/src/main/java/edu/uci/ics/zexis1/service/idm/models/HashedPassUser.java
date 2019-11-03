package edu.uci.ics.zexis1.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HashedPassUser {
    private String email;
    private String hashedPassword;

    @JsonCreator
    public HashedPassUser(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "hashedPassword", required = true) String hashedPassword)
    {
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    @JsonProperty("email")
    public String getEmail(){return email;}


    @JsonProperty("hashedPassword")
    public String getHashedPassword() {
        return hashedPassword;
    }
}
