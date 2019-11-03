package edu.uci.ics.zexis1.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.zexis1.service.billing.core.CustomerCore;
import edu.uci.ics.zexis1.service.billing.core.Validation;
import edu.uci.ics.zexis1.service.billing.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.billing.models.CustomerRequestModel;
import edu.uci.ics.zexis1.service.billing.models.CustomerResponseModel;
import edu.uci.ics.zexis1.service.billing.models.CustomerRetrieveRequestModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("customer")
public class CustomerPage {
    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomer(@Context HttpHeaders httpHeaders, String jsonText){
        ServiceLogger.LOGGER.info("Get request insert a customer: " + jsonText);
        CustomerRequestModel requestModel;
        CustomerResponseModel responseModel = new CustomerResponseModel(-1);
        ObjectMapper mapper = new ObjectMapper();
        try{
            requestModel = mapper.readValue(jsonText, CustomerRequestModel.class);
            String email = requestModel.getEmail();
            String firstName = requestModel.getFirstName();
            String lastName = requestModel.getLastName();
            String ccId = requestModel.getCcId();
            String address = requestModel.getAddress();
            if(!Validation.validCreditCardlength(ccId)){
                responseModel = new CustomerResponseModel(321);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            if(!Validation.validCreditCardValue(ccId)){
                responseModel = new CustomerResponseModel(322);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }

            responseModel = CustomerCore.customerInsert(requestModel);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e){
            if(e instanceof JsonParseException)
                responseModel = new CustomerResponseModel(-3);
            else if(e instanceof JsonMappingException)
                responseModel = new CustomerResponseModel(-2);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
    }
    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@Context HttpHeaders httpHeaders, String jsonText){
        ServiceLogger.LOGGER.info("Get request insert a customer: " + jsonText);
        CustomerRequestModel requestModel;
        CustomerResponseModel responseModel = new CustomerResponseModel(-1);
        ObjectMapper mapper = new ObjectMapper();
        try{
            requestModel = mapper.readValue(jsonText, CustomerRequestModel.class);
            responseModel = CustomerCore.customerUpdate(requestModel);
            return Response.status(Response.Status.OK).entity(responseModel).build();

        } catch (IOException e){
            if(e instanceof JsonParseException)
                responseModel = new CustomerResponseModel(-3);
            else if(e instanceof JsonMappingException)
                responseModel = new CustomerResponseModel(-2);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
    }
    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomer(@Context HttpHeaders httpHeaders, String jsonText){
        ServiceLogger.LOGGER.info("Get request retrieve a customer: " + jsonText);
        CustomerRetrieveRequestModel requestModel;
        CustomerResponseModel responseModel = new CustomerResponseModel(-1);
        ObjectMapper mapper = new ObjectMapper();
        try{
            requestModel = mapper.readValue(jsonText, CustomerRetrieveRequestModel.class);
            String email = requestModel.getEmail();
            responseModel = CustomerCore.retrieveCustomer(email);
            return Response.status(Response.Status.OK).entity(responseModel).build();

        } catch (IOException e){
            if(e instanceof JsonParseException)
                responseModel = new CustomerResponseModel(-3);
            else if(e instanceof JsonMappingException)
                responseModel = new CustomerResponseModel(-2);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
    }

}
