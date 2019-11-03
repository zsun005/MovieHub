package edu.uci.ics.zexis1.service.idm.resources;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.zexis1.service.idm.IDMService;
import edu.uci.ics.zexis1.service.idm.core.VerifySession;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.idm.models.IDMLoginResponseModel;
import edu.uci.ics.zexis1.service.idm.models.VerifySessionRequestModel;
import edu.uci.ics.zexis1.service.idm.models.VerifySessionResponseModel;
import edu.uci.ics.zexis1.service.idm.security.Session;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("session")
public class VerifySessionPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response IMDVerifySession(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received User and Password");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        VerifySessionRequestModel requestModel = null;
        VerifySessionResponseModel responseModel = null;

        try
        {
            int resultCode;
            requestModel = mapper.readValue(jsonText, VerifySessionRequestModel.class);

            ServiceLogger.LOGGER.info("Line 41");
            String sessionID = requestModel.getSessionID();
            String email = requestModel.getEmail();

            ServiceLogger.LOGGER.info("Get Email: " + email);



            if(sessionID.length() != 128)
                responseModel = new VerifySessionResponseModel(-13, null);
            else if(!requestModel.validEmailLength())
                responseModel = new VerifySessionResponseModel(-10, null);
            else if(!requestModel.validEmailFormat())
                responseModel = new VerifySessionResponseModel(-11, null);
            int verifyUser = VerifySession.findUsersAndSessionID(email, sessionID);
            if(responseModel == null)
            {
                if(verifyUser == -1)
                    responseModel = new VerifySessionResponseModel(14, null);
                else if(verifyUser == -2)
                    responseModel = new VerifySessionResponseModel(134, null);
                else{
                    Session session = Session.createSession(email);
                    int status = VerifySession.updateStatusAndTimestamp(session, email, sessionID);
                    ServiceLogger.LOGGER.info("Get Status: " + status);
                    switch(status) {
                        case 1:
                            if(VerifySession.newSessionID != null)
                                responseModel = new VerifySessionResponseModel(130, VerifySession.newSessionID);
                            responseModel = new VerifySessionResponseModel(130, sessionID);
                            break;
                        case 2:
                            responseModel = new VerifySessionResponseModel(132, null);
                            break;
                        case 3:
                            responseModel = new VerifySessionResponseModel(131, null);
                            break;
                        case 4:
                            responseModel = new VerifySessionResponseModel(133, null);
                            break;
                        case -2:
                            responseModel = new VerifySessionResponseModel(134, null);
                            break;
                        default:
                            responseModel = new VerifySessionResponseModel(-1, null);
                    }
                }
            }

            resultCode = responseModel.getResultCode();
            switch (resultCode)
            {
                case -13:
                case -11:
                case -10:
                    return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
                case 14:
                case 130:
                case 131:
                case 132:
                case 133:
                case 134:
                    return Response.status(Response.Status.OK).entity(responseModel).build();
                default:
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            if(e instanceof JsonMappingException){
                responseModel = new VerifySessionResponseModel(-2, null);
                ServiceLogger.LOGGER.info(responseModel.getMessage());
            }
            else if(e instanceof JsonParseException)
                responseModel = new VerifySessionResponseModel(-3, null);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
    }

//    public String generateNewSession(String email){
//        Session newSession = Session.createSession(email);
//        String newSessionID = newSession.getSessionID().toString();
//
//        String newQuery = "INSERT INTO sessions (email, sessionID, timeCreated, lastUsed, exprTime, status) " +
//                "VALUES (?, ?, ?, ?, ?, ?)";
//        try {
//            PreparedStatement newPs = IDMService.getCon().prepareStatement(newQuery);
//            newPs.setString(1, email);
//            newPs.setString(2, newSessionID);
//            newPs.setTimestamp(3, newSession.getTimeCreated());
//            newPs.setTimestamp(4, newSession.getLastUsed());
//            newPs.setTimestamp(5, newSession.getExprTime());
//            newPs.setInt(6, 1);
//            newPs.execute();
//            return newSessionID;
//        } catch (SQLException e){
//            ServiceLogger.LOGGER.info("Insert New Session Falied");
//            return null;
//        }
//    }
}
