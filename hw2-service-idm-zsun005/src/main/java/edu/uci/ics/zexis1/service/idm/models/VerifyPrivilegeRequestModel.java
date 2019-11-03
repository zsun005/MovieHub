package edu.uci.ics.zexis1.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.regex.Pattern;

public class VerifyPrivilegeRequestModel {
    @JsonProperty(value = "email", required = true)
    private String email;
    @JsonProperty(value = "plevel", required = true)
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

    public boolean validPrivilegeLevel()
    {
        return plevel > 0 && plevel <= 5;
    }
}
