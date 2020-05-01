package org.acme.api.reactive;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.acme.entity.attribute.Attribute;
import org.acme.entity.attribute.AttributeReactiveService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/rx/attribute")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AttributeReactiveResource {

	@Inject
    private AttributeReactiveService service;

    @DELETE
    @Path("{attrId}")
    public Response delete(@PathParam("attrId") Long attrId) {
        Boolean deleted = service.delete(attrId) ;
        return deleted ? Response.noContent().build() :
                         Response.status( Response.Status.CONFLICT.getStatusCode(),
                                 "Error: Can't be deleted").build();
    }
    
	@POST
    public Response create(@RequestBody Attribute attribute) {
        Optional<Attribute> savedOp = service.create(attribute);
        return savedOp.isPresent() ? Response.status(Response.Status.CREATED).entity(savedOp.get()).build() :
                                     Response.status(Response.Status.CONFLICT).build();
    }

    @GET
    public Multi<Attribute> getAllPosts() {
        return service.getAll();
        //return items.size() > 0 ? Response.ok( items ).build() :
        //                          Response.status( Response.Status.NOT_FOUND ).build();
    }

    @GET
    @Path("{attrId}")
    public Uni<Attribute> getById(@PathParam("attrId") Long attrId) {
        return service.findById(attrId);
        //return opt.isPresent() ? Response.ok( opt.get() ).build() :
        //                         Response.status(Response.Status.NOT_FOUND).build() ;
    }

    @GET
    @Path("value/{val}")
    public Uni<Attribute> getById(@PathParam("val") String val) {
        return service.findByValue(val);
        //return opt.isPresent() ? Response.ok( opt.get() ).build() :
        //                         Response.status(Response.Status.NOT_FOUND).build() ;
    }


}