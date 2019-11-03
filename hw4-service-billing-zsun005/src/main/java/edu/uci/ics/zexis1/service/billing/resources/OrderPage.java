package edu.uci.ics.zexis1.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.zexis1.service.billing.core.OrderCore;
import edu.uci.ics.zexis1.service.billing.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.billing.models.OrderCompleteRequestModel;
import edu.uci.ics.zexis1.service.billing.models.OrderRequestMode;
import edu.uci.ics.zexis1.service.billing.models.OrderResponseModel;
import edu.uci.ics.zexis1.service.billing.models.OrderRetrieve;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("order")
public class OrderPage {
    @Path("place")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response placeOrder(@Context HttpHeaders httpHeaders, String jsonText){
        OrderResponseModel responseModel = new OrderResponseModel(-1);
        OrderRequestMode requestMode;
        ObjectMapper mapper = new ObjectMapper();

        try{
            requestMode = mapper.readValue(jsonText, OrderRequestMode.class);
            String email = requestMode.getEmail();
            responseModel = OrderCore.placeOrder(email);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e){
            if(e instanceof JsonParseException)
                responseModel = new OrderResponseModel(-3);
            if(e instanceof JsonMappingException)
                responseModel = new OrderResponseModel(-2);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
    }
    @Path("retrieve")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response retrieveOrder(@Context HttpHeaders httpHeaders, String jsonText){
        OrderRetrieve responseModel = new OrderRetrieve(-1);
        OrderRequestMode requestMode;
        ObjectMapper mapper = new ObjectMapper();
        try{
            requestMode = mapper.readValue(jsonText, OrderRequestMode.class);
            String email = requestMode.getEmail();
            responseModel = OrderCore.retrieveOrder(email);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e){
            if(e instanceof JsonParseException)
                responseModel = new OrderRetrieve(-3);
            if(e instanceof JsonMappingException)
                responseModel = new OrderRetrieve(-2);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
    }

    @Path("complete")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response completeOrder(@Context HttpHeaders headers,
                                  @QueryParam("paymentId") String paymentId,
                                  @QueryParam("token") String token,
                                  @QueryParam("PayerID") String PayerID){
        ServiceLogger.LOGGER.info("Get paymentID: " + paymentId);
        ServiceLogger.LOGGER.info("Get token: " + token);
        ServiceLogger.LOGGER.info("Get PayerID " + PayerID);

        ServiceLogger.LOGGER.info("Get request completeOrder");
        OrderCompleteRequestModel requestModel;
        OrderResponseModel responseModel = null;

        requestModel = new OrderCompleteRequestModel(paymentId, token, PayerID);
        responseModel = OrderCore.completePayment(requestModel);
        int resultCode = responseModel.getResultCode();
        if(resultCode != -1)
            return Response.status(Response.Status.OK).entity(responseModel).build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();

    }

}
