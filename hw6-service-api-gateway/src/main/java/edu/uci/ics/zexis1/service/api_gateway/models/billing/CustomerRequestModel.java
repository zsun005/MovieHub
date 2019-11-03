package edu.uci.ics.zexis1.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.api_gateway.models.RequestModel;

public class CustomerRequestModel extends RequestModel {
    @JsonProperty(value = "email", required = true)
    String email;
    @JsonProperty(value = "firstName", required = true)
    String firstName;
    @JsonProperty(value = "lastName", required = true)
    String lastName;
    @JsonProperty(value = "ccId", required = true)
    String ccId;
    @JsonProperty(value = "address", required = true)
    String address;

    @JsonCreator
    public CustomerRequestModel(@JsonProperty(value = "email", required = true) String email,
                                @JsonProperty(value = "firstName", required = true) String firstName,
                                @JsonProperty(value = "lastName", required = true) String lastName,
                                @JsonProperty(value = "ccId", required = true) String ccId,
                                @JsonProperty(value = "address", required = true) String address) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ccId = ccId;
        this.address = address;
    }
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }
    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }
    @JsonProperty("ccId")
    public String getCcId() {
        return ccId;
    }
    @JsonProperty("address")
    public String getAddress() {
        return address;
    }
}
