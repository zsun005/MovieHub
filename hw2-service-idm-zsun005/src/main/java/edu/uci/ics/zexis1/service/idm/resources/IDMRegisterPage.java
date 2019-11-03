package edu.uci.ics.zexis1.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.idm.models.HashedPassUser;
import edu.uci.ics.zexis1.service.idm.models.IDMRegisterRequestModel;
import edu.uci.ics.zexis1.service.idm.models.IDMRegisterResponseModel;
import edu.uci.ics.zexis1.service.idm.security.Crypto;
import edu.uci.ics.zexis1.service.idm.core.emailContainsCheckandInsert;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;

@Path("register")
public class IDMRegisterPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response IDMRegister(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received User and Password.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();
        IDMRegisterRequestModel requestModel = null;
        IDMRegisterResponseModel responseModel = null;

        try{
            int resultCode;
            requestModel = mapper.readValue(jsonText, IDMRegisterRequestModel.class);

//            for(int i = 0; i < pword.length; i++)
//                System.out.print(pword[i]);
//            String p = new String(pword);
//
//            System.out.println("\n" + p);


            if(!requestModel.validPasswordLengthNullRequirement())
                responseModel = new IDMRegisterResponseModel(-12);
            else if(!requestModel.validEmailLength())
                responseModel = new IDMRegisterResponseModel(-10);
            else if(!requestModel.validEmailFormat())
                responseModel = new IDMRegisterResponseModel(-11);
            else if(!requestModel.validPasswordLength())
                responseModel = new IDMRegisterResponseModel(12);
            else if(!requestModel.validPasswordChar())
                responseModel = new IDMRegisterResponseModel(13);
            else if(emailContainsCheckandInsert.queryCheckEmail(requestModel.getEmail()))
                responseModel = new IDMRegisterResponseModel(16);
            else {
                responseModel = new IDMRegisterResponseModel(110);
                byte[] salt = Crypto.genSalt();
                char[] pword = requestModel.getPassword();
                byte[] hashedPassword = Crypto.hashPassword(pword, salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH);
                String password = getHashedPass(hashedPassword);
                HashedPassUser hPuser = new HashedPassUser(requestModel.getEmail(), password);
                String sSalt = getHashedPass(salt);
                emailContainsCheckandInsert.insertEmailtoDB(hPuser, 5, sSalt, 1);
            }
            resultCode = responseModel.getResultCode();
            switch (resultCode)
            {
                case -12:
                case -11:
                case -10:
                    return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
                case 12:
                case 13:
                case 16:
                case 110:
                    return Response.status(Response.Status.OK).entity(responseModel).build();
                default:
                    responseModel = new IDMRegisterResponseModel(-1);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
            }



        }
        catch (IOException e)
        {
            e.printStackTrace();
            if(e instanceof JsonMappingException){
                responseModel = new IDMRegisterResponseModel(-2);
                ServiceLogger.LOGGER.info(responseModel.getMessage());
            }
            else if(e instanceof JsonParseException)
                responseModel = new IDMRegisterResponseModel(-3);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
    }

//    private char[] strArrayToCharArray(String[] strArrary)
//    {
//        char[] to_return = new char[strArrary.length];
//        for(int i = 0; i < strArrary.length; i++)
//            to_return[i] = strArrary[i].toCharArray()[0];
//        return to_return;
//    }

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
