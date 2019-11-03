package edu.uci.ics.zexis1.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IDMRegisterRequestModel {
    @JsonProperty(value = "email", required = true)
    private String email;
    @JsonProperty(value = "password", required = true)
    private char[] password;

    @JsonCreator
    public IDMRegisterRequestModel(@JsonProperty(value = "email", required = true) String email,
                                   @JsonProperty(value = "password", required = true) char[] password) {
        this.email = email;
        this.password = password;
    }
    @JsonProperty(value = "email", required = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty(value = "password", required = true)
    public char[] getPassword() {
        ServiceLogger.LOGGER.info("GETPass: " + password.toString());
        return password;
    }

    public void setPassword(char[] password) {
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
    public boolean validPasswordChar()
    {
        // TODO: 1. upper, lower, number, special character
        // [!@#$%^&*(),.?":{}|<>]
        String specialChars = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        boolean upperflag = false;
        boolean lowerflag = false;
        boolean numberflag = false;
        boolean specialflag = false;

        for(int i = 0; i < password.length; i++)
        {
            if(Character.isDigit(password[i]))
                numberflag = true;
            else if(Character.isUpperCase(password[i]))
                upperflag = true;
            else if(Character.isLowerCase(password[i]))
                lowerflag = true;
            else if(specialChars.contains(Character.toString(password[i])))
                specialflag = true;
        }

        return numberflag && upperflag && lowerflag && specialflag;
    }





}
