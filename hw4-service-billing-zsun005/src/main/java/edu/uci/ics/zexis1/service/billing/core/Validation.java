package edu.uci.ics.zexis1.service.billing.core;

import java.util.Date;

import java.util.regex.Pattern;

public class Validation {

    public static boolean validEmailFormat(String email)
    {
        String re = "^[a-zA-Z0-9_+&*-]+(?:\\."  +
                "[a-zA-Z0-9_+&*-]+)*@"      +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(re);
        return pattern.matcher(email).matches();
    }

    public static boolean validEmailLength(String email)
    {
        return email.length() > 0 && email.length() <= 50;
    }

    public static boolean validQuantity(Integer quantity) {return quantity > 0;}

    public static boolean validCreditCardValue(String id){
        String re = "\\d+";

        Pattern pattern = Pattern.compile(re);

        return pattern.matcher(id).matches();
    }

    public static boolean validCreditCardlength(String id){
        return 16 <= id.length() && id.length() <= 20;
    }

    public static boolean validExprrationDate(java.sql.Date date){
        java.util.Date d = new  java.util.Date(date.getTime());
        java.util.Date currTime = new java.util.Date();
        currTime.getTime();
        return d.after(currTime);
    }
}
