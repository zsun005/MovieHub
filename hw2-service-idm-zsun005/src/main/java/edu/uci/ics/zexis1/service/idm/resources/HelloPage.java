package edu.uci.ics.zexis1.service.idm.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import edu.uci.ics.zexis1.service.idm.logger.ServiceLogger;

@Path("test")
public class HelloPage {
    @Path("hello")
    @GET
    public Response helloWorld() {
        ServiceLogger.LOGGER.info("Hello world!");
        return Response.status(Status.OK).build();
    }
}
