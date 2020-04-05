package org.acme.api;

import org.acme.entity.Actor;
import org.acme.entity.Attribute;
import org.acme.entity.AttributeService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/attribute")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AttributeResource {

	@Inject
    private AttributeService service;

    @DELETE
    @Path("{key}/{value}")
    public Response deleteByKeyValue(@PathParam("key") String key, @PathParam("value") String value) {
        Boolean deleted = service.delete(key, value) ;
        final Response response = deleted ? Response.noContent().build() :
                Response.status(Response.Status.CONFLICT.getStatusCode(),
                        "Error: Can't be deleted").build();
        return response;
    }

    @DELETE
    @Path("{guid}")
    public Response deleteByGuid(@PathParam("guid") UUID guid) {
        Boolean deleted = service.delete(guid) ;
        final Response response = deleted ? Response.noContent().build() :
                Response.status(Response.Status.CONFLICT.getStatusCode(),
                        "Error: Can't be deleted").build();
        return response;
    }
    
	@POST
    public Response create(@RequestBody Attribute attribute) {
        Optional<Attribute> savedOp = service.create(attribute);
        return savedOp.isPresent() ? Response.ok( savedOp.get() ).build() :
                                     Response.status( Response.Status.CONFLICT).build();
    }

    @GET
    public Response getAllPosts() {
        return Response.ok( this.service.getAll() ).build();
    }

    @GET
    @Path("{guid}")
    public Response getById(@PathParam("guid") UUID guid) {
        Optional<Attribute> opt = service.findByGuid(guid);
        return opt.isPresent() ? Response.ok( opt.get() ).build() :
                                 Response.status(Response.Status.NOT_FOUND).build() ;
    }


}