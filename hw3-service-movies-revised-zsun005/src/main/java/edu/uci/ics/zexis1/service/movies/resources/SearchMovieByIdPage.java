package edu.uci.ics.zexis1.service.movies.resources;

import edu.uci.ics.zexis1.service.movies.core.SearchMovieID;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.SearchMovieByIdResponseModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("get")
public class SearchMovieByIdPage {
    @GET
    @Path("{movieid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovieById(@Context HttpHeaders headers, @PathParam("movieid") String movieid){
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("Recevied Search Movie BY id Request");
        ServiceLogger.LOGGER.info("Email: " + email);
        ServiceLogger.LOGGER.info("SessionID: " + sessionID);
        ServiceLogger.LOGGER.info("GET ID: " +  movieid);

        if(movieid.length() != 9 && movieid.length() != 10){
            SearchMovieByIdResponseModel responseModel = new SearchMovieByIdResponseModel(211, null);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        SearchMovieByIdResponseModel responseModel = SearchMovieID.SearchMovieById(movieid, email);
        ServiceLogger.LOGGER.info("ResultCode: " + responseModel.getResultCode());
        ServiceLogger.LOGGER.info("Message: " + responseModel.getMessage());
        int resultCode = responseModel.getResultCode();
        if(resultCode == -1)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

}
