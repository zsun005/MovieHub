package edu.uci.ics.zexis1.service.api_gateway.resources;

import edu.uci.ics.zexis1.service.api_gateway.GatewayService;
import edu.uci.ics.zexis1.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.zexis1.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.zexis1.service.api_gateway.models.idm.IDMSessionRequestModel;
import edu.uci.ics.zexis1.service.api_gateway.models.idm.PrivilegeRequestModel;
import edu.uci.ics.zexis1.service.api_gateway.models.idm.RegisterUserRequestModel;
import edu.uci.ics.zexis1.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.zexis1.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.zexis1.service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("idm")
public class IDMEndpoints {
    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUserRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to register user.");
        RegisterUserRequestModel requestModel;

        try{
            requestModel = (RegisterUserRequestModel) ModelValidator.verifyModel(jsonText, RegisterUserRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RegisterUserRequestModel.class);
        }
        String email = requestModel.getEmail();
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setTransactionID(transactionID);
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPUserRegister());
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        cr.setHttpType("POST");
        GatewayService.getThreadPool().getQueue().enqueue(cr);



        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
//


//        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);
//        return Response.status(Status.NO_CONTENT).entity(responseModel).build();

    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUserRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Recevied request to loginUser Request.");
        RegisterUserRequestModel requestModel;

        try{
            requestModel = (RegisterUserRequestModel) ModelValidator.verifyModel(jsonText, RegisterUserRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RegisterUserRequestModel.class);
        }

        String transcationID = TransactionIDGenerator.generateTransactionID();
        String email = requestModel.getEmail();

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPUserLogin());
        cr.setRequest(requestModel);
        cr.setTransactionID(transcationID);
        cr.setHttpType("POST");

        GatewayService.getThreadPool().add(cr);
        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID",transcationID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
//
//        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transcationID);
//
//        return Response.status(Status.NO_CONTENT).entity(responseModel).build();
    }

    @Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifySessionRequest(String jsonText){
        ServiceLogger.LOGGER.info("Recevied request to session.");
        IDMSessionRequestModel requestModel;

        try{
            requestModel = (IDMSessionRequestModel) ModelValidator.verifyModel(jsonText, IDMSessionRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, IDMSessionRequestModel.class);
        }

        String transcationID = TransactionIDGenerator.generateTransactionID();
        String email = requestModel.getEmail();

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPSessionVerify());
        cr.setHttpType("POST");
        cr.setRequest(requestModel);
        cr.setTransactionID(transcationID);
        GatewayService.getThreadPool().add(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID",transcationID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
    }

    @Path("privilege")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyUserPrivilegeRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Recevied request to privilege.");
        PrivilegeRequestModel requestModel;

        try{
            requestModel = (PrivilegeRequestModel) ModelValidator.verifyModel(jsonText, PrivilegeRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, PrivilegeRequestModel.class);
        }

        String transcationID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        String email = requestModel.getEmail();
        cr.setEmail(email);
        cr.setHttpType("POST");
        cr.setURI(GatewayService.getIdmConfigs().getIdmUri());
        cr.setEndpoint(GatewayService.getIdmConfigs().getEPUserPrivilegeVerify());
        cr.setRequest(requestModel);
        cr.setTransactionID(transcationID);

        GatewayService.getThreadPool().add(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID",transcationID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

    }
}
