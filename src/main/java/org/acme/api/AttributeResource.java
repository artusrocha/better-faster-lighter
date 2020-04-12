package org.acme.api;

import org.acme.entity.attribute.Attribute;
import org.acme.entity.attribute.AttributeService;
import org.acme.entity.category.Category;
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
    public Response getAllPosts() {
        List<Attribute> items = service.getAll();
        return items.size() > 0 ? Response.ok( items ).build() :
                                  Response.status( Response.Status.NOT_FOUND ).build();
    }

    @GET
    @Path("{attrId}")
    public Response getById(@PathParam("attrId") Long attrId) {
        Optional<Attribute> opt = service.findById(attrId);
        return opt.isPresent() ? Response.ok( opt.get() ).build() :
                                 Response.status(Response.Status.NOT_FOUND).build() ;
    }

    @GET
    @Path("value/{val}")
    public Response getById(@PathParam("val") String val) {
        Optional<Attribute> opt = service.findByValue(val);
        return opt.isPresent() ? Response.ok( opt.get() ).build() :
                                 Response.status(Response.Status.NOT_FOUND).build() ;
    }


}