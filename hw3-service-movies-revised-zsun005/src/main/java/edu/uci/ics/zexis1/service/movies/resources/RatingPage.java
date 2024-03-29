package edu.uci.ics.zexis1.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.zexis1.service.movies.core.RatingCore;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.AddGenreReqeustModel;
import edu.uci.ics.zexis1.service.movies.models.AddRatingRequestModel;
import edu.uci.ics.zexis1.service.movies.models.BasicResponseModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("rating")
public class RatingPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRating(@Context HttpHeaders headers, String jsonText){
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");


        ServiceLogger.LOGGER.info("Recevied Rating Update Request");
        ServiceLogger.LOGGER.info("Email: " + email);
        ServiceLogger.LOGGER.info("SessionID: " + sessionID);

        AddRatingRequestModel reqeustModel;
        BasicResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        try{
            reqeustModel = mapper.readValue(jsonText, AddRatingRequestModel.class);
            responseModel = RatingCore.updateRating(reqeustModel);
            int resultCode = responseModel.getResultCode();
            if(resultCode == -1)
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e){
            if(e instanceof JsonParseException) {
                responseModel = new BasicResponseModel(-3);
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            if(e instanceof JsonMappingException) {
                responseModel = new BasicResponseModel(-2);
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
