package edu.uci.ics.zexis1.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;

import java.util.regex.Pattern;

public class UserModel {

    @JsonProperty(value = "email", required = true)
    private String email;
    @JsonProperty(value = "password", required = true)
    private char[] password;


    @JsonCreator
    public UserModel(@JsonProperty(value = "email", required = true) String email,
                     @JsonProperty(value = "password", required = true) char[] password) {
        this.email = email;
        this.password = password;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
    @JsonCreator
    public void setEmail(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }

    @JsonProperty("password")
    public char[] getPassword() {
        ServiceLogger.LOGGER.info("GETPASSWORD Function: " + password.toString());
        return password;
    }
    @JsonCreator
    public void setPassword(@JsonProperty(value = "password", required = true) char[] password) {
        this.password = password;
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

    public boolean validPasswordLength()
    {
        // TODO length >= 7 and <= 16

        return password.length <= 16 && password.length >= 7;
    }
    public boolean validPasswordLengthNullRequirement()
    {
        // TODO: 1. Invalid length: null or empty
        return password != null && password.length != 0;
    }

}
