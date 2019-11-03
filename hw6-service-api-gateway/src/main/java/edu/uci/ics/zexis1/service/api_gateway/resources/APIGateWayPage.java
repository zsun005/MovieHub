package edu.uci.ics.zexis1.service.api_gateway.resources;

import edu.uci.ics.zexis1.service.api_gateway.GatewayService;
import edu.uci.ics.zexis1.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.api_gateway.models.NoContentResponseModel;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("report")
public class APIGateWayPage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiGateway(@Context HttpHeaders headers){
        ServiceLogger.LOGGER.info("Received request for HTTP response.");
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("TransactionID: " + transactionID);
        String query = "SELECT response, httpstatus FROM responses WHERE transactionid = ?";
        try{
            PreparedStatement ps = GatewayService.getConPool().requestCon().prepareStatement(query);
            ps.setString(1, transactionID);
            ServiceLogger.LOGGER.info("Try Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String response = rs.getString("response");
                Integer status = rs.getInt("httpstatus");
                Response.Status realStatus;
                if(status == 200)
                    realStatus = Response.Status.OK;
                else if(status == 400)
                    realStatus = Response.Status.BAD_REQUEST;
                else
                     realStatus = Response.Status.INTERNAL_SERVER_ERROR;
                ServiceLogger.LOGGER.info("Return httpstatus: " + status);
                if(response != null){
                    return Response.status(realStatus).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").entity(response).build();
                }

            }
            //NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);
            Response response = Response.status(Response.Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Origin", "*").header("transactionID", transactionID).header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).build();//entity(responseModel).build();
            ServiceLogger.LOGGER.info("Returning response: " + response);
            return response;
//            return Response.status(Response.Status.NO_CONTENT).entity(responseModel).header("sessionID", sessionID).header("transactionID",transactionID).header("requestDelay",GatewayService.getGatewayConfigs().getRequestDelay()).build();
        } catch (SQLException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
