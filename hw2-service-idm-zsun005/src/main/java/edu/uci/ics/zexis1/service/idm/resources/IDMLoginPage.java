package edu.uci.ics.zexis1.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.zexis1.service.idm.core.IDMLoginCore;
import edu.uci.ics.zexis1.service.idm.core.SessionInsert;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.idm.models.IDMInvalidLoginResponseModel;
import edu.uci.ics.zexis1.service.idm.models.IDMLoginResponseModel;
import edu.uci.ics.zexis1.service.idm.models.UserModel;
import edu.uci.ics.zexis1.service.idm.core.emailContainsCheckandInsert;

import edu.uci.ics.zexis1.service.idm.security.Crypto;
import edu.uci.ics.zexis1.service.idm.security.Session;
import edu.uci.ics.zexis1.service.idm.security.Token;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;

@Path("login")
public class IDMLoginPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response IMDLogin(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received User and Password");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        UserModel requestModel = null;
        IDMLoginResponseModel responseModel = null;
        IDMInvalidLoginResponseModel invalidLoginResponseModel = null;

        try
        {
            int resultCode;
            requestModel = mapper.readValue(jsonText, UserModel.class);


//            for(int i = 0; i < pword.length; i++)
//                System.out.print(pword[i]);
//            String p = new String(pword);
//
//            System.out.println("\n" + p);



            if(!(requestModel.validPasswordLengthNullRequirement()&&requestModel.validPasswordLength()))
                responseModel = new IDMLoginResponseModel(-12);
            else if(!requestModel.validEmailLength())
                responseModel = new IDMLoginResponseModel(-10);
            else if(!requestModel.validEmailFormat())
                responseModel = new IDMLoginResponseModel(-11);
            else if(!requestModel.validPasswordLength()) // TODO: need to CHECK!
                responseModel = new IDMLoginResponseModel(-12);
            else if(!emailContainsCheckandInsert.queryCheckEmail(requestModel.getEmail()))
                responseModel = new IDMLoginResponseModel(14);

            if(responseModel == null) {
                char[] pword = requestModel.getPassword();
                String email = requestModel.getEmail();
                String salt = IDMLoginCore.queryCheckEmail(email);
                ServiceLogger.LOGGER.info("Get Salt: " + salt);
                String password = getHashPassword(pword, salt);
                if(!salt.equals("")) {
                    if (!IDMLoginCore.emailAndPassCheck(email, password)) {
                        ServiceLogger.LOGGER.info("Password not match");
                        responseModel = new IDMLoginResponseModel(11);
                    }
                    else
                    {
                        // User and Password are both valid
                        ServiceLogger.LOGGER.info("Login Success!");
                        Session session = Session.createSession(email);
                        String sessionID = session.getSessionID().toString();
                        responseModel = new IDMLoginResponseModel(120);
                        responseModel.setSessionID(sessionID);
                        SessionInsert.insertNewSession(session, email, sessionID);
                    }
                }

            }

            resultCode = responseModel.getResultCode();
            invalidLoginResponseModel = new IDMInvalidLoginResponseModel(resultCode, responseModel.getMessage());
            switch (resultCode)
            {
                case -12:
                case -11:
                case -10:
                    return Response.status(Response.Status.BAD_REQUEST).entity(invalidLoginResponseModel).build();
                case 11:
                case 14:
                    return Response.status(Response.Status.OK).entity(invalidLoginResponseModel).build();
                case 120:
                    return Response.status(Response.Status.OK).entity(responseModel).build();

                default:
                    responseModel = new IDMLoginResponseModel(-1);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(invalidLoginResponseModel).build();



            }


        }
        catch (IOException e)
        {
            e.printStackTrace();
            if(e instanceof JsonMappingException){
                responseModel = new IDMLoginResponseModel(-2);
                invalidLoginResponseModel = new IDMInvalidLoginResponseModel(-2, responseModel.getMessage());
                ServiceLogger.LOGGER.info(responseModel.getMessage());
            }
            else if(e instanceof JsonParseException) {
                responseModel = new IDMLoginResponseModel(-3);
                invalidLoginResponseModel = new IDMInvalidLoginResponseModel(-3, responseModel.getMessage());
            }

            return Response.status(Response.Status.BAD_REQUEST).entity(invalidLoginResponseModel).build();
        }
    }
    private String getHashPassword(char[] pword, String sSalt)
    {
        // TODO: Salt is wrong, need to find Salt store in DB.
        byte[] salt = Token.convert(sSalt);
        for(int i = 0; i < salt.length; i++) {
            System.out.print(salt[i]);
        }
        System.out.println();
        byte[] hashedPassword = Crypto.hashPassword(pword, salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH);
        String password = getHashedPass(hashedPassword);
        return password;
    }
    private String getHashedPass(byte[] hashedPassword) {
        StringBuffer buf = new StringBuffer();
        for (byte b : hashedPassword) {
            buf.append(format(Integer.toHexString(Byte.toUnsignedInt(b))));
        }
        return buf.toString();
    }

    private String format(String binS) {
        int length = 2 - binS.length();
        char[] padArray = new char[length];
        Arrays.fill(padArray, '0');
        String padString = new String(padArray);
        return padString + binS;
    }

}
