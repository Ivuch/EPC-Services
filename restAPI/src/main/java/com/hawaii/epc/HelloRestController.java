package com.hawaii.epc;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("/hellos")
public class HelloRestController {

    @Inject
    AuthorizationIdentityStore authIdentityStore ;

    @GET
    @RolesAllowed("ADMI")
    @Produces(MediaType.APPLICATION_JSON)
    public String sayHello() {
        return "{\"message\":\"Hello Jakarta REST API! from Hawaii\"}";
    }
}
