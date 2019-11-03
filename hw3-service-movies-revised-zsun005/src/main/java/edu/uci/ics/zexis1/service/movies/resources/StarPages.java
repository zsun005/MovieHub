package edu.uci.ics.zexis1.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.uci.ics.zexis1.service.movies.core.SearchStarCore;
import edu.uci.ics.zexis1.service.movies.core.StarCore;
import edu.uci.ics.zexis1.service.movies.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.movies.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("star")
public class StarPages {
    @Path("search")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchStar(@Context HttpHeaders headers, @QueryParam("name") String name,
                               @QueryParam("birthYear") Integer birthYear, @QueryParam("movieTitle") String movieTitle,
                               @QueryParam("limit") @DefaultValue("10") int limit, @QueryParam("orderby") @DefaultValue("name") String orderby,
                               @QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("direction") @DefaultValue("ASC") String direction)
    {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        StarResponseModel responseModel;
        ServiceLogger.LOGGER.info("Recevied Request");
        ServiceLogger.LOGGER.info("Email: " + email);
        ServiceLogger.LOGGER.info("SessionID: " + sessionID);
        ServiceLogger.LOGGER.info("Title: " + movieTitle);
        ServiceLogger.LOGGER.info("BirthYear: " + birthYear);
        ServiceLogger.LOGGER.info("Offset: " + offset);
        ServiceLogger.LOGGER.info("Limit: " + limit);
        ServiceLogger.LOGGER.info("direction: " + direction);
        ServiceLogger.LOGGER.info("OrderBy: " + orderby);

        SearchStarRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("movieTitle", movieTitle);
        objectNode.put("name", name);
        objectNode.put("birthYear", birthYear);
        objectNode.put("offset", offset);
        objectNode.put("limit", limit);
        objectNode.put("direction", direction);
        objectNode.put("orderby", orderby);

        ServiceLogger.LOGGER.info("Input Json node generated: " + objectNode);
        JsonNode jsonNode = mapper.createArrayNode();

        try{
            String jsonText = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
            ServiceLogger.LOGGER.info("Create Json Text: " + jsonText);
            requestModel = mapper.readValue(jsonText, SearchStarRequestModel.class);

            responseModel = SearchStarCore.SearchStars(requestModel);

            int resultCase = responseModel.getResultCode();

            if(resultCase == 212 || resultCase == 213)
                return Response.status(Response.Status.OK).entity(responseModel).build();

            responseModel = new StarResponseModel(-1, null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();


        }
        catch (IOException e)
        {
            if(e instanceof JsonParseException) {
                responseModel = new StarResponseModel(-3, null);
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            if(e instanceof JsonMappingException) {
                responseModel = new StarResponseModel(-2, null);
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchStarById(@Context HttpHeaders headers, @PathParam("id") String id){
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        StarResponseModel responseModel = StarCore.searchStarById(id, email);
        int resultCode = responseModel.getResultCode();
        if(resultCode == -1)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStar(@Context HttpHeaders headers, String jsonText){
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        BasicResponseModel responseModel;
        AddStarRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();
        try{
            requestModel = mapper.readValue(jsonText, AddStarRequestModel.class);
            responseModel = StarCore.addNewStar(requestModel, email);
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

    @Path("starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarToMovie(@Context HttpHeaders headers, String jsonText){
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        BasicResponseModel responseModel;
        AddStarToMovieRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();
        try{
            requestModel = mapper.readValue(jsonText, AddStarToMovieRequestModel.class);
            responseModel = StarCore.addStarToMovie(requestModel, email);
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
