package edu.uci.ics.zexis1.service.movies.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.zexis1.service.movies.MovieService;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.VerifyPrivilegeRequestModel;
import edu.uci.ics.zexis1.service.movies.models.VerifyPrivilegeResponseModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class VerifyPrivillege {
    public static VerifyPrivilegeResponseModel VerifyPrivillege(String email, int privilege){
        ServiceLogger.LOGGER.info("Verifying privilege level with IDM...");

        ServiceLogger.LOGGER.info("Movie VerifyPrivillege: " + email + "if has privilege " + privilege);

        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        String IDM_URI = MovieService.getMovieConfigs().getIdmConfigs().getIdmUri();

        String IDM_ENDPOINT_PATH = MovieService.getMovieConfigs().getIdmConfigs().getPrivilegePath();

        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT_PATH);

        Invocation.Builder invocationBuild = webTarget.request(MediaType.APPLICATION_JSON);

        VerifyPrivilegeRequestModel requestModel = new VerifyPrivilegeRequestModel(email, privilege);

        Response response = invocationBuild.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));


        // Converting JsonText to response Model
        ServiceLogger.LOGGER.info("Revepived status 200!");
        String json = response.readEntity(String.class);
        ServiceLogger.LOGGER.info("jsonText: " + json);
        VerifyPrivilegeResponseModel responseModel = getInfoFromJson(json);


        return responseModel;

    }
    private static VerifyPrivilegeResponseModel getInfoFromJson(String json)
    {
        try {
            ObjectMapper mapper = new ObjectMapper();
            VerifyPrivilegeResponseModel responseModel = mapper.readValue(json, VerifyPrivilegeResponseModel.class);
            return responseModel;
        }
        catch (IOException e)
        {
            ServiceLogger.LOGGER.info("Reading Jsontext Error");
            return null;
        }
    }
}
