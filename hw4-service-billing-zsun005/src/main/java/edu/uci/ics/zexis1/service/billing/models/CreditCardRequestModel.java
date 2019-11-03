package edu.uci.ics.zexis1.service.billing.models;

import com.fasterxml.jackson.annotation.*;

import java.sql.Date;

public class CreditCardRequestModel {
    @JsonProperty(value = "id", required = true)
    String id;
    @JsonProperty(value = "firstName", required = true)
    String firstName;
    @JsonProperty(value = "lastName", required = true)
    String lastName;
    @JsonProperty(value = "expiration", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String expiration;

    @JsonCreator
    public CreditCardRequestModel(@JsonProperty(value = "id", required = true) String id,
                                  @JsonProperty(value = "firstName", required = true) String firstName,
                                  @JsonProperty(value = "lastName", required = true) String lastName,
                                  @JsonProperty(value = "expiration", required = true)
                                  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") String expiration) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiration = expiration;

    }
    @JsonProperty(value = "id")
    public String getId() {
        return id;
    }
    @JsonProperty(value = "firstName")
    public String getFirstName() {
        return firstName;
    }
    @JsonProperty(value = "lastName")
    public String getLastName() {
        return lastName;
    }
    @JsonProperty("expiration")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public String getExpiration() {
        return expiration;
    }
}
