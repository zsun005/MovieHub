package edu.uci.ics.zexis1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.regex.Pattern;

public class VerifyPrivilegeRequestModel {
    private String email;
    private int plevel;

    @JsonCreator
    public VerifyPrivilegeRequestModel(@JsonProperty(value = "email", required = true) String email,
                                       @JsonProperty(value = "plevel", required = true) int plevel)
    {
        this.email = email;
        this.plevel = plevel;
    }
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
    @JsonProperty("plevel")
    public int getPlevel() {
        return plevel;
    }
}