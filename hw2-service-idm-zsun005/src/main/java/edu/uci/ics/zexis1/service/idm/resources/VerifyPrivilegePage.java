package edu.uci.ics.zexis1.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import edu.uci.ics.zexis1.service.idm.core.VerifyPrivilege;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;
import edu.uci.ics.zexis1.service.idm.models.VerifyPrivilegeRequestModel;
import edu.uci.ics.zexis1.service.idm.models.VerifyPrivilegeResponseModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("privilege")
public class VerifyPrivilegePage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyPrivilegePage(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received User and Password");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);
        ObjectMapper mapper = new ObjectMapper();

        VerifyPrivilegeRequestModel requestModel = null;
        VerifyPrivilegeResponseModel responseModel = null;

        try
        {
            int resultCode;
            requestModel = mapper.readValue(jsonText, VerifyPrivilegeRequestModel.class);
            String email = requestModel.getEmail();
            int pleveltoCmp = requestModel.getPlevel();

            if(!requestModel.validPrivilegeLevel())
                responseModel = new VerifyPrivilegeResponseModel(-14);
            else if(!requestModel.validEmailLength())
                responseModel = new VerifyPrivilegeResponseModel(-10);
            else if(!requestModel.validEmailFormat())
                responseModel = new VerifyPrivilegeResponseModel(-11);

            if(responseModel == null)
            {
                int userPlevel = VerifyPrivilege.getPrivilegeLevel(email);
                if(userPlevel <= 0)
                    responseModel = new VerifyPrivilegeResponseModel(14);
                else if(userPlevel <= pleveltoCmp)
                    responseModel = new VerifyPrivilegeResponseModel(140);
                else
                    responseModel = new VerifyPrivilegeResponseModel(141);

            }
            resultCode = responseModel.getResultCode();
            switch (resultCode)
            {
                case -14:
                case -11:
                case -10:
                    return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
                case 14:
                case 140:
                case 141:
                    return Response.status(Response.Status.OK).entity(responseModel).build();
                    default:
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new VerifyPrivilegeResponseModel(-1)).build();

            }
        }
        catch (IOException e)
        {
            ServiceLogger.LOGGER.info("Exception::" + e.getClass());
            e.printStackTrace();
            if(e instanceof JsonMappingException)
                responseModel = new VerifyPrivilegeResponseModel(-2);
            if(e instanceof JsonParseException )
                responseModel = new VerifyPrivilegeResponseModel(-3);

            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
    }
}
