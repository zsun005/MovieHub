package edu.uci.ics.zexis1.service.api_gateway.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.uci.ics.zexis1.service.api_gateway.GatewayService;
import edu.uci.ics.zexis1.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.zexis1.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.zexis1.service.api_gateway.models.VerifySessionResponseModel;
import edu.uci.ics.zexis1.service.api_gateway.models.idm.PrivilegeRequestModel;
import edu.uci.ics.zexis1.service.api_gateway.models.idm.RegisterUserRequestModel;
import edu.uci.ics.zexis1.service.api_gateway.models.movie.*;
import edu.uci.ics.zexis1.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.zexis1.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.zexis1.service.api_gateway.utilities.TransactionIDGenerator;
import edu.uci.ics.zexis1.service.api_gateway.utilities.VerifySession;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("movies")
public class MovieEndpoints {
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovieRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo
//                                       @QueryParam("title") String title, @QueryParam("genre") String genre,
//                                       @QueryParam("year") @DefaultValue("0") int year, @QueryParam("director") String director,
//                                       @QueryParam("hidden") Boolean hidden, @QueryParam("limit") @DefaultValue("10") int limit,
//                                       @QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("orderby") @DefaultValue("rating") String orderby,
//                                       @QueryParam("direction") @DefaultValue("DESC") String direction
                                       ) {
        ServiceLogger.LOGGER.info("Recevied request to Search Movie");
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }

        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);
        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        sessionID = verifySessionResponseModel.getSessionID();

        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        String query = uriInfo.getRequestUri().getQuery();
        query = query.trim();
        query = query.replaceAll("\\s", "%20");
        cr.setQueryParam(query);
        cr.setSessionID(sessionID);
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieSearch() + "?");
        ServiceLogger.LOGGER.info("EndPoint: " + cr.getEndpoint());
        ServiceLogger.LOGGER.info("Query: " + cr.getQueryParam());
        cr.setHttpType("GET");
        cr.setRequest(null);
        cr.setTransactionID(transactionID);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", sessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();




    }

    @Path("get/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieRequest(@Context HttpHeaders headers, @PathParam("movieid") String movieid) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();
        ServiceLogger.LOGGER.info("printHeader");
        printHeader(email, sessionID, transactionID);



        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }

        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);
        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        sessionID = verifySessionResponseModel.getSessionID();

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setSessionID(sessionID);
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieGet());
        cr.setQueryParam(movieid);
        ServiceLogger.LOGGER.info("GET Movie using path: " + cr.getEndpoint());
        cr.setHttpType("GET");
        cr.setRequest(null);
        cr.setTransactionID(transactionID);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", sessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovieRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        printHeader(email, sessionID, transactionID);
        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }
        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);

        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        sessionID = verifySessionResponseModel.getSessionID();
        AddMovieRequestModel requestModel;

        try{
            requestModel = (AddMovieRequestModel) ModelValidator.verifyModel(jsonText, AddMovieRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, PrivilegeRequestModel.class);
        }

        ClientRequest cr = new ClientRequest();
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setSessionID(sessionID);
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieAdd());
        cr.setTransactionID(transactionID);
        ServiceLogger.LOGGER.info("Add Movie Endpoint URI: " + cr.getEndpoint());
        cr.setHttpType("POST");

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", sessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

    }

    @Path("delete/{movieid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieRequest(@Context HttpHeaders headers, @PathParam("movieid") String movieid) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }

        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);
        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        sessionID = verifySessionResponseModel.getSessionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieDelete());
        cr.setQueryParam(movieid);
        ServiceLogger.LOGGER.info("GET Movie using path: " + cr.getEndpoint());
        cr.setHttpType("DELETE");
        cr.setRequest(null);
        cr.setTransactionID(transactionID);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", sessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

    }

    @Path("genre")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresRequest(@Context HttpHeaders headers) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }

        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);
        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        sessionID = verifySessionResponseModel.getSessionID();
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setSessionID(sessionID);
        cr.setEmail(email);
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPGenreGet());
        cr.setQueryParam("");
        ServiceLogger.LOGGER.info("Search Movie Endpoint URI: " + cr.getEndpoint());
        cr.setHttpType("GET");
        cr.setRequest(null);
        cr.setTransactionID(transactionID);

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", sessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
    }

    @Path("genre/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenreRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }
        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);

        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        sessionID = verifySessionResponseModel.getSessionID();

        AddGenreRequestModel requestModel;
        try{
            requestModel = (AddGenreRequestModel) ModelValidator.verifyModel(jsonText, AddGenreRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, PrivilegeRequestModel.class);
        }

        ClientRequest cr = new ClientRequest();
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setSessionID(sessionID);
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPGenreAdd());
        cr.setTransactionID(transactionID);
        ServiceLogger.LOGGER.info("Add Genre Endpoint URI: " + cr.getEndpoint());
        cr.setHttpType("POST");

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", sessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

    }

    @Path("genre/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresForMovieRequest(@Context HttpHeaders headers, @PathParam("movieid") String movieid) {

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }

        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);
        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        sessionID = verifySessionResponseModel.getSessionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setSessionID(sessionID);
        cr.setEmail(email);
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPGenreGet());
        cr.setQueryParam("");
        ServiceLogger.LOGGER.info("GET Genre using path: " + cr.getEndpoint());
        cr.setHttpType("GET");
        cr.setRequest(null);
        cr.setTransactionID(transactionID);

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", sessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

    }

    @Path("star/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response starSearchRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }

        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);
        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        sessionID = verifySessionResponseModel.getSessionID();

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setSessionID(sessionID);
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieSearch() + "?");
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        String query = uriInfo.getRequestUri().getQuery();
        query = query.trim();
        query = query.replaceAll("\\s", "%20");
        cr.setQueryParam(query);
        ServiceLogger.LOGGER.info("Search star Endpoint: " + cr.getEndpoint());
        cr.setHttpType("GET");
        cr.setRequest(null);
        cr.setTransactionID(transactionID);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", sessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

    }

    @Path("star/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStarRequest(@Context HttpHeaders headers, @PathParam("id") String id) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }

        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);
        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        sessionID = verifySessionResponseModel.getSessionID();

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setSessionID(sessionID);
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarGet());
        cr.setQueryParam(id);
        ServiceLogger.LOGGER.info("GET Star using path: " + cr.getEndpoint());
        cr.setHttpType("GET");
        cr.setRequest(null);
        cr.setTransactionID(transactionID);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", sessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

    }

    @Path("star/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }
        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);

        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        sessionID = verifySessionResponseModel.getSessionID();
        AddStarRequestModel requestModel;

        try{
            requestModel = (AddStarRequestModel) ModelValidator.verifyModel(jsonText, AddStarRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, PrivilegeRequestModel.class);
        }

        ClientRequest cr = new ClientRequest();
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setSessionID(sessionID);
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarAdd());
        cr.setTransactionID(transactionID);
        ServiceLogger.LOGGER.info("Add Star Endpoint URI: " + cr.getEndpoint());
        cr.setHttpType("POST");

        GatewayService.getThreadPool().getQueue().enqueue(cr);


        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", sessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

    }

    @Path("star/starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarToMovieRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }
        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);

        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        sessionID = verifySessionResponseModel.getSessionID();
        AddStarToMovieRequestModel requestModel;
        try{
            requestModel = (AddStarToMovieRequestModel) ModelValidator.verifyModel(jsonText, AddStarToMovieRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, PrivilegeRequestModel.class);
        }
        ClientRequest cr = new ClientRequest();
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setSessionID(sessionID);
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarIn());
        cr.setTransactionID(transactionID);
        ServiceLogger.LOGGER.info("Star in Endpoint URI: " + cr.getEndpoint());
        cr.setHttpType("POST");

        GatewayService.getThreadPool().getQueue().enqueue(cr);


        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", sessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();


    }

    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRatingRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }
        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);

        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        sessionID = verifySessionResponseModel.getSessionID();
        AddRatingRequestModel requestModel;
        try{
            requestModel = (AddRatingRequestModel) ModelValidator.verifyModel(jsonText, AddRatingRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, PrivilegeRequestModel.class);
        }
        ClientRequest cr = new ClientRequest();
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setSessionID(sessionID);
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPRating());
        cr.setTransactionID(transactionID);
        ServiceLogger.LOGGER.info("Rating Endpoint URI: " + cr.getEndpoint());
        cr.setHttpType("POST");

        GatewayService.getThreadPool().getQueue().enqueue(cr);


        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", sessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();


    }

    private void printHeader(String email, String sessionID, String transactionID){
        ServiceLogger.LOGGER.info("email: " + email);
        ServiceLogger.LOGGER.info("sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("transactionID: " + transactionID);
    }
}
