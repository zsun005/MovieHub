package edu.uci.ics.zexis1.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.zexis1.service.movies.utilities.ResultCases;

public class BasicResponseModel {

    @JsonProperty(value = "resultCode", required = true)
    int resultCode;
    @JsonProperty(value = "message", required = true)
    String message;

    @JsonCreator
    public BasicResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        this.message = ResultCases.generalResultMessage(this.resultCode);
    }
    @JsonProperty("resultCode")
    public int getResultCode() {
        return resultCode;
    }
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}
