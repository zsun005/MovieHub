package edu.uci.ics.zexis1.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.zexis1.service.movies.core.AddGenre;
import edu.uci.ics.zexis1.service.movies.core.RetrieveGenre;
import edu.uci.ics.zexis1.service.movies.models.AddGenreReqeustModel;
import edu.uci.ics.zexis1.service.movies.models.AddMovieResponseModel;
import edu.uci.ics.zexis1.service.movies.models.BasicResponseModel;
import edu.uci.ics.zexis1.service.movies.models.GenreRetrieveResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("genre")
public class RetrieveGenrePage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveGenre(@Context HttpHeaders headers){
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        GenreRetrieveResponseModel responseModel = RetrieveGenre.retrieveGenre(email);
        int resultCode = responseModel.getResultCode();
        if(resultCode == -1)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenre(@Context HttpHeaders headers, String jsonText){
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        AddGenreReqeustModel reqeustModel;
        BasicResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();

        try{
            reqeustModel = mapper.readValue(jsonText, AddGenreReqeustModel.class);
            responseModel = AddGenre.addGenre(reqeustModel, email);
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

    @Path("{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveGenresByMovieId(@Context HttpHeaders headers, @QueryParam("movieid") String movieid){
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        GenreRetrieveResponseModel responseModel = RetrieveGenre.retrieveGenreByMovieId(movieid, email);
        int resultCode = responseModel.getResultCode();
        if(resultCode == -1)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }



}
