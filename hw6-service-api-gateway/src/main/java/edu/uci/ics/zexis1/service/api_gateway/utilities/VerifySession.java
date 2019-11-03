package edu.uci.ics.zexis1.service.api_gateway.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.zexis1.service.api_gateway.GatewayService;
import edu.uci.ics.zexis1.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.api_gateway.models.VerifySessionRequestModel;
import edu.uci.ics.zexis1.service.api_gateway.models.VerifySessionResponseModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class VerifySession {
    public static VerifySessionResponseModel VerifySession(String email, String sessionID){
        ServiceLogger.LOGGER.info("Verifying Session with IDM...");

        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        String URI = GatewayService.getIdmConfigs().getIdmUri();
        String ENDPOINT_PATH = GatewayService.getIdmConfigs().getEPSessionVerify();

        WebTarget webTarget = client.target(URI).path(ENDPOINT_PATH);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        VerifySessionRequestModel requestModel = new VerifySessionRequestModel(email, sessionID);
        Response response = invocationBuilder.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));

        ServiceLogger.LOGGER.info("Recevied status");
        String json = response.readEntity(String.class);
        ServiceLogger.LOGGER.info("JsonText: " + json);
        VerifySessionResponseModel responseModel;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            responseModel = objectMapper.readValue(json, VerifySessionResponseModel.class);
            return responseModel;
        } catch (IOException e){
            ServiceLogger.LOGGER.info("Reading Json Error");
            return null;
        }

    }
}
