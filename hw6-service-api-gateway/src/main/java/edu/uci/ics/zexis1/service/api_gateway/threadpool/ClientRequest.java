package edu.uci.ics.zexis1.service.api_gateway.threadpool;

import edu.uci.ics.zexis1.service.api_gateway.models.RequestModel;

public class ClientRequest {
    private String email;
    private String sessionID;
    private String transactionID;
    private RequestModel request;
    private String URI;
    private String endpoint;
    private String httpType;
    private String queryParam;

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    public ClientRequest() {}

    public ClientRequest(String email, String sessionID, String transactionID, RequestModel request, String URI, String endpoint, String httpType, String queryParam) {
        this.email = email;
        this.sessionID = sessionID;
        this.transactionID = transactionID;
        this.request = request;
        this.URI = URI;
        this.endpoint = endpoint;
        this.httpType = httpType;
        this.queryParam = queryParam;
    }

    public ClientRequest(String email, String sessionID, String transactionID, RequestModel request, String URI, String endpoint, String httpType) {
        this.email = email;
        this.sessionID = sessionID;
        this.transactionID = transactionID;
        this.request = request;
        this.URI = URI;
        this.endpoint = endpoint;
        this.httpType = httpType;
    }

    public String getHttpType() {
        return httpType;
    }

    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }

    public String getEmail() {
        return email;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public RequestModel getRequest() {
        return request;
    }

    public String getURI() {
        return URI;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setRequest(RequestModel request) {
        this.request = request;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
