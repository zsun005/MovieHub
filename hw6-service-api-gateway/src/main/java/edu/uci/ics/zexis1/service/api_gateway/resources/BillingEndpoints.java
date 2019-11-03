package edu.uci.ics.zexis1.service.api_gateway.resources;

import edu.uci.ics.zexis1.service.api_gateway.GatewayService;
import edu.uci.ics.zexis1.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.zexis1.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.zexis1.service.api_gateway.models.VerifySessionResponseModel;
import edu.uci.ics.zexis1.service.api_gateway.models.billing.*;
import edu.uci.ics.zexis1.service.api_gateway.models.idm.RegisterUserRequestModel;
import edu.uci.ics.zexis1.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.zexis1.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.zexis1.service.api_gateway.utilities.ResultCodes;
import edu.uci.ics.zexis1.service.api_gateway.utilities.TransactionIDGenerator;
import edu.uci.ics.zexis1.service.api_gateway.utilities.VerifySession;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("billing")
public class BillingEndpoints {
    @Path("cart/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertToCartRequest(@Context HttpHeaders headers, String jsonText) {
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
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to cart insert");
        ShoppingCartRequestModel requestModel;

        try{
            requestModel = (ShoppingCartRequestModel) ModelValidator.verifyModel(jsonText, ShoppingCartRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RegisterUserRequestModel.class);
        }

        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartInsert());
        cr.setRequest(requestModel);
        cr.setHttpType("POST");
        cr.setTransactionID(transactionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();



    }

    @Path("cart/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCartRequest(@Context HttpHeaders headers, String jsonText) {
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
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to cart update");
        ShoppingCartRequestModel requestModel;

        try{
            requestModel = (ShoppingCartRequestModel) ModelValidator.verifyModel(jsonText, ShoppingCartRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RegisterUserRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setHttpType("POST");
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartUpdate());
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

//        return Response.status(Response.Status.NO_CONTENT).header("transactionID", transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
//
//
    }

    @Path("cart/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCartRequest(@Context HttpHeaders headers, String jsonText) {
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
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to cart update");
        ShoppingCartRequestModel requestModel;

        try{
            requestModel = (ShoppingCartRequestModel) ModelValidator.verifyModel(jsonText, ShoppingCartRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RegisterUserRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartDelete());
        cr.setHttpType("POST");
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

//        return Response.status(Response.Status.NO_CONTENT).header("transactionID", transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

    }

    @Path("cart/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Retrieve request for cart retrieve");
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(responseModel).build();
        }

        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);
        int resultCode = verifySessionResponseModel.getResultCode();
        ServiceLogger.LOGGER.info("GET resultCode: " + resultCode);
        if(resultCode != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to cart update");
        ShoppingCartRequestModel requestModel;

        try{
            requestModel = (ShoppingCartRequestModel) ModelValidator.verifyModel(jsonText, ShoppingCartRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RegisterUserRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartRetrieve());
        cr.setHttpType("POST");
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();


//        return Response.status(Response.Status.NO_CONTENT).header("transactionID", transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
//
    }

    @Path("cart/clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCartRequest(@Context HttpHeaders headers, String jsonText) {
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
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to cart update");
        ShoppingCartRequestModel requestModel;

        try{
            requestModel = (ShoppingCartRequestModel) ModelValidator.verifyModel(jsonText, ShoppingCartRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RegisterUserRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCartUpdate());
        cr.setRequest(requestModel);
        cr.setHttpType("POST");
        cr.setTransactionID(transactionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

//        return Response.status(Response.Status.NO_CONTENT).header("transactionID", transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
//
    }

    @Path("creditcard/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
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
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to creditcard insert");
        CreditCardRequestModel requestModel;

        try{
            requestModel = (CreditCardRequestModel) ModelValidator.verifyModel(jsonText, CreditCardRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, CreditCardRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcInsert());
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        cr.setHttpType("POST");
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

    }

    @Path("creditcard/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
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
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to creditcard insert");
        CreditCardRequestModel requestModel;

        try{
            requestModel = (CreditCardRequestModel) ModelValidator.verifyModel(jsonText, CreditCardRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, CreditCardRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcUpdate());
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        cr.setHttpType("POST");
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();


//
    }

    @Path("creditcard/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
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
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to creditcard delete");
        CreditCardRequestModel requestModel;

        try{
            requestModel = (CreditCardRequestModel) ModelValidator.verifyModel(jsonText, CreditCardRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, CreditCardRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcDelete());
        cr.setHttpType("POST");
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        cr.setTransactionID(transactionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

//        return Response.status(Response.Status.NO_CONTENT).header("transactionID", transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
//
    }

    @Path("creditcard/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
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
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to creditcard retrieve");
        CreditCardRequestModel requestModel;

        try{
            requestModel = (CreditCardRequestModel) ModelValidator.verifyModel(jsonText, CreditCardRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, CreditCardRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCcRetrieve());
        cr.setHttpType("POST");
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

//        return Response.status(Response.Status.NO_CONTENT).header("transactionID", transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
//
    }

    @Path("customer/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomerRequest(@Context HttpHeaders headers, String jsonText) {
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
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to customer insert");
        CustomerRequestModel requestModel;

        try{
            requestModel = (CustomerRequestModel) ModelValidator.verifyModel(jsonText, CustomerRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, CustomerRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerInsert());
        cr.setHttpType("POST");
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

//        return Response.status(Response.Status.NO_CONTENT).header("transactionID", transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
//
    }

    @Path("customer/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerRequest(@Context HttpHeaders headers, String jsonText) {
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
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to customer update");
        CustomerRequestModel requestModel;

        try{
            requestModel = (CustomerRequestModel) ModelValidator.verifyModel(jsonText, CustomerRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, CustomerRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerUpdate());
        cr.setHttpType("POST");
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();


//        return Response.status(Response.Status.NO_CONTENT).header("transactionID", transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
//
    }

    @Path("customer/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Recevied request for customer retrieve");
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
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to customer update");
        CustomerRetrieveRequestModel requestModel;

        try{
            requestModel = (CustomerRetrieveRequestModel) ModelValidator.verifyModel(jsonText, CustomerRetrieveRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, CustomerRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPCustomerRetrieve());
        cr.setHttpType("POST");
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

//        return Response.status(Response.Status.NO_CONTENT).header("transactionID", transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
//
    }

    @Path("order/place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrderRequest(@Context HttpHeaders headers, String jsonText) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        if(sessionID == null){
            VerifySessionResponseModel responseModel = new VerifySessionResponseModel(-17, "SessionID not provided in request header.", null);
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        VerifySessionResponseModel verifySessionResponseModel = VerifySession.VerifySession(email, sessionID);
        if(verifySessionResponseModel.getResultCode() != 130){
            return Response.status(Status.OK).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(verifySessionResponseModel).build();
        }
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to order place");
        OrderRequestModel requestModel;

        try{
            requestModel = (OrderRequestModel) ModelValidator.verifyModel(jsonText, OrderRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, OrderRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPOrderPlace());
        cr.setEmail(email);
        cr.setSessionID(newSessionID);

        cr.setHttpType("POST");
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

//        return Response.status(Response.Status.NO_CONTENT).header("transactionID", transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
//
    }

    @Path("order/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrderRequest(@Context HttpHeaders headers, String jsonText) {
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
        String newSessionID = verifySessionResponseModel.getSessionID();
        ServiceLogger.LOGGER.info("Recevied request to order place");
        OrderRequestModel requestModel;

        try{
            requestModel = (OrderRequestModel) ModelValidator.verifyModel(jsonText, OrderRequestModel.class);
        } catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, OrderRequestModel.class);
        }
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getBillingConfigs().getBillingUri());
        cr.setEndpoint(GatewayService.getBillingConfigs().getEPOrderRetrieve());
        cr.setEmail(email);
        cr.setSessionID(newSessionID);
        cr.setHttpType("POST");
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionID);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("sessionID", newSessionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();

//        return Response.status(Response.Status.NO_CONTENT).header("transactionID", transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
//
    }
}
