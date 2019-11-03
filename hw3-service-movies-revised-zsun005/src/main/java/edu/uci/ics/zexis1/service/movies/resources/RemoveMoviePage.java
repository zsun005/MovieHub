package edu.uci.ics.zexis1.service.movies.resources;

import edu.uci.ics.zexis1.service.movies.core.removeMovie;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.BasicResponseModel;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("delete")
public class RemoveMoviePage {
    @Path("{movieid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeMovie(@Context HttpHeaders headers, @PathParam("movieid") String movieid){
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("Recevied Delete Movie BY id Request");
        ServiceLogger.LOGGER.info("Email: " + email);
        ServiceLogger.LOGGER.info("SessionID: " + sessionID);
        ServiceLogger.LOGGER.info("GET ID: " +  movieid);

        BasicResponseModel responseModel = removeMovie.removeMovie(movieid, email);
        int resultCode = responseModel.getResultCode();
        if(resultCode == -1){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }
}
