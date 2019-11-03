package edu.uci.ics.zexis1.service.movies.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.uci.ics.zexis1.service.movies.core.SearchMovie;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.SearchMoviesRequestModel;
import edu.uci.ics.zexis1.service.movies.models.SearchMoviesResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("search")
public class SearchMoviePage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovies(@Context HttpHeaders headers,
                                 @QueryParam("title") String title, @QueryParam("genre") String genre,
                                 @QueryParam("year") @DefaultValue("0") int year, @QueryParam("director") String director,
                                 @QueryParam("hidden") @DefaultValue("false") Boolean hidden, @QueryParam("limit") @DefaultValue("10") int limit,
                                 @QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("orderby") @DefaultValue("rating") String orderby,
                                 @QueryParam("direction") @DefaultValue("DESC") String direction){

        SearchMoviesRequestModel requestModel;
        SearchMoviesResponseModel responseModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("Recevied Request");
        ServiceLogger.LOGGER.info("Email: " + email);
        ServiceLogger.LOGGER.info("SessionID: " + sessionID);
        ServiceLogger.LOGGER.info("Title: " + title);
        ServiceLogger.LOGGER.info("Genre: " + genre);
        ServiceLogger.LOGGER.info("Year: " + year);
        ServiceLogger.LOGGER.info("Director: " + director);
        ServiceLogger.LOGGER.info("Hidden: " + hidden);
        ServiceLogger.LOGGER.info("Offset: " + offset);
        ServiceLogger.LOGGER.info("Limit: " + limit);
        ServiceLogger.LOGGER.info("Direction: " + direction);
        ServiceLogger.LOGGER.info("OrderBy: " + orderby);

        if(offset % limit != 0)
            offset = 0;

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("title", title);
        objectNode.put("genre", genre);
        objectNode.put("year", year);
        objectNode.put("director", director);
        objectNode.put("hidden", hidden);
        objectNode.put("offset", offset);
        objectNode.put("limit", limit);
        objectNode.put("direction", direction);
        objectNode.put("orderby", orderby);

        ServiceLogger.LOGGER.info("Input Json node generated: " + objectNode);
        JsonNode jsonNode = mapper.createObjectNode();

        try{
            String jsonText = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
            ServiceLogger.LOGGER.info("Create Json Text: " + jsonText);
            requestModel = mapper.readValue(jsonText, SearchMoviesRequestModel.class);

            responseModel = SearchMovie.searchMovie(requestModel, email);

            if(responseModel.getResultCode() == -1)
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e){
            ServiceLogger.LOGGER.info("Error Mapping Json Text");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
