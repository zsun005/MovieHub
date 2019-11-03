package edu.uci.ics.zexis1.service.billing.resources;



import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.zexis1.service.billing.core.CreditCard;
import edu.uci.ics.zexis1.service.billing.core.Validation;
import edu.uci.ics.zexis1.service.billing.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.billing.models.CreditCardIDModel;
import edu.uci.ics.zexis1.service.billing.models.CreditCardRequestModel;
import edu.uci.ics.zexis1.service.billing.models.CreditCardResponseModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Path("creditcard")
public class CreditCardPage {
    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCreditCard(@Context HttpHeaders httpHeader, String jsonText){

        ServiceLogger.LOGGER.info("Get CreditCard insert request...");
        ServiceLogger.LOGGER.info(jsonText);
        CreditCardRequestModel requestModel;
        CreditCardResponseModel responseModel = new CreditCardResponseModel(-1);

        try{
            ObjectMapper mapper = new ObjectMapper();

            requestModel = mapper.readValue(jsonText, CreditCardRequestModel.class);
            String id = requestModel.getId();
            String firstName = requestModel.getFirstName();
            String lastName = requestModel.getLastName();
            ServiceLogger.LOGGER.info("Get id" + id);
            ServiceLogger.LOGGER.info("Get firstName " + firstName);
            ServiceLogger.LOGGER.info("Get lastName" + lastName);

            String expriation = requestModel.getExpiration();
            Date date = Date.valueOf(expriation);

            ServiceLogger.LOGGER.info("Get expriation " + date.toString());
            if(id == null || date == null || firstName == null || lastName == null)
                throw new JsonMappingException("");
            if(!Validation.validCreditCardlength(id)){
                responseModel = new CreditCardResponseModel(321);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else if(!Validation.validCreditCardValue(id)) {
                responseModel = new CreditCardResponseModel(322);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else if(!Validation.validExprrationDate(date))
            {
                responseModel = new CreditCardResponseModel(323);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }

            responseModel = CreditCard.insertCreditCard(requestModel, date);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e){
            ServiceLogger.LOGGER.info("Execption: " + e.getClass());
            e.printStackTrace();
            if(e instanceof JsonParseException)
                responseModel = new CreditCardResponseModel(-3);
            else //if(e instanceof JsonParseException)
                responseModel = new CreditCardResponseModel(-2);
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }


    }
    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCard(@Context HttpHeaders httpHeaders, String jsonText){
        ServiceLogger.LOGGER.info("Get CreditCard update request...");
        ServiceLogger.LOGGER.info(jsonText);
        CreditCardRequestModel requestModel;
        CreditCardResponseModel responseModel = new CreditCardResponseModel(-1);

        try{
            ObjectMapper mapper = new ObjectMapper();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            mapper.setDateFormat(dateFormat);
            requestModel = mapper.readValue(jsonText, CreditCardRequestModel.class);
            String id = requestModel.getId();
            ServiceLogger.LOGGER.info("Line 78");

            String exp = requestModel.getExpiration();

            Date date = Date.valueOf(exp);
            String lastName = requestModel.getLastName();
            String firstName = requestModel.getFirstName();

            if(id == null || date == null || firstName == null || lastName == null)
                throw new JsonMappingException("");

            if(!Validation.validCreditCardlength(id)){
                responseModel = new CreditCardResponseModel(321);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else if(!Validation.validCreditCardValue(id)) {
                responseModel = new CreditCardResponseModel(322);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else if(!Validation.validExprrationDate(date))
            {
                responseModel = new CreditCardResponseModel(323);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }

            responseModel = CreditCard.updateCreditCard(requestModel, date);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e){
            if(e instanceof JsonParseException)
                responseModel = new CreditCardResponseModel(-3);
            else if(e instanceof JsonMappingException)
                responseModel = new CreditCardResponseModel(-2);
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
    }
    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditcard(@Context HttpHeaders httpHeaders, String jsonText){
        ServiceLogger.LOGGER.info("Get CreditCard update request...");
        ServiceLogger.LOGGER.info(jsonText);
        CreditCardIDModel requestModel;
        CreditCardResponseModel responseModel = new CreditCardResponseModel(-1);
        try{
            ObjectMapper mapper = new ObjectMapper();
            requestModel = mapper.readValue(jsonText, CreditCardIDModel.class);
            String id = requestModel.getId();
            if(!Validation.validCreditCardlength(id))
                responseModel = new CreditCardResponseModel(321);
            else if(!Validation.validCreditCardValue(id))
                responseModel = new CreditCardResponseModel(322);
            else{
                responseModel = CreditCard.deleteCreditCard(id);
            }
            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e){
            if(e instanceof JsonParseException)
                responseModel = new CreditCardResponseModel(-3);
            else if(e instanceof JsonMappingException)
                responseModel = new CreditCardResponseModel(-2);
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
    }

    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditcard(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Get CreditCard retrieve request...");
        ServiceLogger.LOGGER.info(jsonText);
        CreditCardIDModel requestModel;
        CreditCardResponseModel responseModel = null;
        try{
            ObjectMapper mapper = new ObjectMapper();
            requestModel = mapper.readValue(jsonText, CreditCardIDModel.class);
            String id = requestModel.getId();
            ServiceLogger.LOGGER.info("Get Id: " + id);
            if(!Validation.validCreditCardlength(id))
                responseModel = new CreditCardResponseModel(321);
            else if(!Validation.validCreditCardValue(id))
                responseModel = new CreditCardResponseModel(322);
            else
                responseModel = CreditCard.retrieveCreditcard(id);

            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (IOException e){
            if(e instanceof JsonParseException) {
                responseModel = new CreditCardResponseModel(-3);
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            if(e instanceof JsonMappingException) {
                responseModel = new CreditCardResponseModel(-2);
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }
}
