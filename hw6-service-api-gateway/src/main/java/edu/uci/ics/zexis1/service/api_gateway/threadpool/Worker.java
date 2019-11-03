package edu.uci.ics.zexis1.service.api_gateway.threadpool;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.zexis1.service.api_gateway.GatewayService;
import edu.uci.ics.zexis1.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.api_gateway.models.Model;
import edu.uci.ics.zexis1.service.api_gateway.models.RequestModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Worker extends Thread {
    int id;
    ThreadPool threadPool;

    private Worker(int id, ThreadPool threadPool) {
        this.id = id;
        this.threadPool = threadPool;
    }

    public static Worker CreateWorker(int id, ThreadPool threadPool) {
        Worker to_return = new Worker(id, threadPool);
        return to_return;
    }

    private Model getInfoFromJson(String json){
        try{
            ObjectMapper mapper = new ObjectMapper();
            Model model = mapper.readValue(json, Model.class);
            return model;
        } catch (IOException e){
            ServiceLogger.LOGGER.info("Reading Jsontext Error");
            return null;
        }
    }

    public void process() {
//        ServiceLogger.LOGGER.info("Process");
        ClientRequest clientRequest = threadPool.remove();
        if(clientRequest == null)
            return;
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        Response response;

        String method = clientRequest.getHttpType();
        if(method == "POST") {
            WebTarget webTarget = client.target(clientRequest.getURI()).path(clientRequest.getEndpoint());

            Invocation.Builder invocationBuild = webTarget.request(MediaType.APPLICATION_JSON).header("email", clientRequest.getEmail()).header("sessionID", clientRequest.getSessionID()).header("transactionID", clientRequest.getTransactionID());

            RequestModel requestModel = clientRequest.getRequest();

            response = invocationBuild.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        }
        else if(method == "GET"){
            WebTarget webTarget = client.target(clientRequest.getURI() + clientRequest.getEndpoint() + clientRequest.getQueryParam());
            ServiceLogger.LOGGER.info("GET METHOD URI: " + webTarget.getUri());

            ServiceLogger.LOGGER.info("email: " + clientRequest.getEmail());
            ServiceLogger.LOGGER.info("sessionID: " + clientRequest.getSessionID());
            ServiceLogger.LOGGER.info("transactionID: " + clientRequest.getTransactionID());
            Invocation.Builder invocationBuild = webTarget.request(MediaType.APPLICATION_JSON).header("email", clientRequest.getEmail()).header("sessionID", clientRequest.getSessionID()).header("transactionID", clientRequest.getTransactionID());
            ServiceLogger.LOGGER.info("GET Method");
            Invocation invocation = invocationBuild.buildGet();
            response = invocation.invoke();
        }
        else{
            WebTarget webTarget = client.target(clientRequest.getURI() + clientRequest.getEndpoint() + clientRequest.getQueryParam());

            Invocation.Builder invocationBuild = webTarget.request(MediaType.APPLICATION_JSON).header("email", clientRequest.getEmail()).header("sessionID", clientRequest.getSessionID()).header("transactionID", clientRequest.getTransactionID());
            ServiceLogger.LOGGER.info("DELETE Method");
            Invocation invocation = invocationBuild.buildDelete();
            response = invocation.invoke();
        }


        int status = response.getStatus();

        ServiceLogger.LOGGER.info("Recevied status: " + status);

        String json = response.readEntity(String.class);

        Connection con = GatewayService.getConPool().requestCon();

        String query = "INSERT INTO responses (transactionid, email, sessionid, response, httpstatus) VALUES (?, ?, ?, ?, ?)";

        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, clientRequest.getTransactionID());
            ps.setString(2, clientRequest.getEmail());
            ps.setString(3, clientRequest.getSessionID());
            ps.setString(4, json);
            ps.setInt(5, status);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ps.execute();
            ServiceLogger.LOGGER.info("Query Succeed");
        } catch (SQLException e){
            ServiceLogger.LOGGER.info("Query Failed");
        }
        GatewayService.getConPool().releaseCon(con);

    }

    @Override
    public void run() {
        while (true) {
            process();
        }
    }
}
