package edu.uci.ics.zexis1.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.zexis1.service.billing.core.ShoppingCart;
import edu.uci.ics.zexis1.service.billing.core.Validation;
import edu.uci.ics.zexis1.service.billing.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.billing.models.ShoppingCartRequestModel;
import edu.uci.ics.zexis1.service.billing.models.ShoppingCartResponseModel;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("cart")
public class ShoppingCartPage {
    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertAPI(String jsonText){
        ServiceLogger.LOGGER.info("Get Cart Insert Request...");
        ServiceLogger.LOGGER.info(jsonText);
        ShoppingCartRequestModel requestModel;
        ShoppingCartResponseModel responseModel = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            requestModel = mapper.readValue(jsonText, ShoppingCartRequestModel.class);
            String email = requestModel.getEmail();
            String movieId = requestModel.getMovieId();
            Integer quantity = requestModel.getQuantity();
            ServiceLogger.LOGGER.info("Get email" + email);
            ServiceLogger.LOGGER.info("GEt movieId" + movieId);
            ServiceLogger.LOGGER.info("GET quantity" + quantity);
            if(quantity == null || email == null || movieId == null)
                throw new JsonMappingException("");
            if(!Validation.validEmailFormat(email))
                responseModel = new ShoppingCartResponseModel(-11);
            if(!Validation.validEmailLength(email))
                responseModel = new ShoppingCartResponseModel(-10);
            if(!Validation.validQuantity(quantity)) {
                responseModel = new ShoppingCartResponseModel(33);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }

            if(responseModel != null)
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();

            responseModel = ShoppingCart.insertIntoCarts(email, movieId, quantity);

            return Response.status(Response.Status.OK).entity(responseModel).build();

        }catch (JsonMappingException | JsonParseException e){
            if(e instanceof JsonParseException)
                responseModel = new ShoppingCartResponseModel(-3);
            if(e instanceof JsonMappingException)
                responseModel = new ShoppingCartResponseModel(-2);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (IOException e){
            return null;
        }
    }
    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCart(String jsonText) {
        ServiceLogger.LOGGER.info("Get Cart Update Request...");
        ServiceLogger.LOGGER.info(jsonText);
        ShoppingCartRequestModel requestModel;
        ShoppingCartResponseModel responseModel = null;
        try{
            ObjectMapper mapper = new ObjectMapper();
            requestModel = mapper.readValue(jsonText, ShoppingCartRequestModel.class);

            String email = requestModel.getEmail();
            String movieId = requestModel.getMovieId();

            Integer quantity = requestModel.getQuantity();
            if(quantity == null || email == null || movieId == null)
                throw new JsonMappingException("");

            if(!Validation.validEmailFormat(email))
                responseModel = new ShoppingCartResponseModel(-11);
            if(!Validation.validEmailLength(email))
                responseModel = new ShoppingCartResponseModel(-10);
            if(!Validation.validQuantity(quantity)) {
                responseModel = new ShoppingCartResponseModel(33);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            if(responseModel != null)
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();

            responseModel = ShoppingCart.updateShoppingCart(email, movieId, quantity);

            return Response.status(Response.Status.OK).entity(responseModel).build();


        } catch (IOException e){
            if(e instanceof JsonParseException)
                responseModel = new ShoppingCartResponseModel(-3);
            if(e instanceof JsonMappingException)
                responseModel = new ShoppingCartResponseModel(-2);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
    }

    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCart(String jsonText)
    {
        ServiceLogger.LOGGER.info("Get Cart Delete Request...");
        ServiceLogger.LOGGER.info(jsonText);

        ShoppingCartRequestModel requestModel;
        ShoppingCartResponseModel responseModel = null;

        try{
            ObjectMapper mapper = new ObjectMapper();
            requestModel = mapper.readValue(jsonText, ShoppingCartRequestModel.class);
            String email = requestModel.getEmail();
            String movieId = requestModel.getMovieId();
            if(movieId == null)
                throw new JsonMappingException("");

            if(!Validation.validEmailFormat(email))
                responseModel = new ShoppingCartResponseModel(-11);
            if(!Validation.validEmailLength(email))
                responseModel = new ShoppingCartResponseModel(-10);

            if(responseModel != null)
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            responseModel = ShoppingCart.deleteShoppingCart(email, movieId);

            return Response.status(Response.Status.OK).entity(responseModel).build();

        } catch (IOException e){
            if(e instanceof JsonParseException)
                responseModel = new ShoppingCartResponseModel(-3);
            if(e instanceof JsonMappingException)
                responseModel = new ShoppingCartResponseModel(-2);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }


    }

    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCart(String jsonText)
    {
        ServiceLogger.LOGGER.info("Get Cart Retrieve Request...");
        ServiceLogger.LOGGER.info(jsonText);

        ShoppingCartRequestModel requestModel;
        ShoppingCartResponseModel responseModel = null;


        try{
            ObjectMapper mapper = new ObjectMapper();
            requestModel = mapper.readValue(jsonText, ShoppingCartRequestModel.class);
            String email = requestModel.getEmail();
            if(!Validation.validEmailFormat(email))
                responseModel = new ShoppingCartResponseModel(-11);
            if(!Validation.validEmailLength(email))
                responseModel = new ShoppingCartResponseModel(-10);


            if(responseModel != null)
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();

            responseModel = ShoppingCart.retrieveCart(email);

            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        catch (IOException e)
        {
            if(e instanceof JsonParseException)
                responseModel = new ShoppingCartResponseModel(-3);
            if(e instanceof JsonMappingException)
                responseModel = new ShoppingCartResponseModel(-2);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
    }
    @Path("clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearShoppingCart(String jsonText){
        ServiceLogger.LOGGER.info("Recevied Shopping Cart clear request");
        ServiceLogger.LOGGER.info("Query: " + jsonText);

        ShoppingCartRequestModel requestModel;
        ShoppingCartResponseModel responseModel = null;

        try
        {
            ObjectMapper mapper = new ObjectMapper();
            requestModel = mapper.readValue(jsonText, ShoppingCartRequestModel.class);
            String email = requestModel.getEmail();

            if(!Validation.validEmailFormat(email))
                responseModel = new ShoppingCartResponseModel(-11);
            if(!Validation.validEmailLength(email))
                responseModel = new ShoppingCartResponseModel(-10);


            if(responseModel != null)
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();

            responseModel = ShoppingCart.clearShoppingCart(email);

            return Response.status(Response.Status.OK).entity(responseModel).build();


        } catch (IOException e){
            if(e instanceof JsonParseException)
                responseModel = new ShoppingCartResponseModel(-3);
            if(e instanceof JsonMappingException)
                responseModel = new ShoppingCartResponseModel(-2);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();

        }

    }

}
