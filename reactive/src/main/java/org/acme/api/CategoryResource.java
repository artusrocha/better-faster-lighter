package org.acme.api;

import org.acme.entity.category.Category;
import org.acme.entity.category.CategoryService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

	@Inject
    private CategoryService service;

    @DELETE
    @Path("{categoryId}")
    public Response delete(@PathParam("categoryId") Long categoryId) {
        Boolean deleted = service.delete(categoryId) ;
        return deleted ? Response.noContent().build() :
                         Response.status( Response.Status.CONFLICT.getStatusCode(),
                                 "Error: Can't be deleted").build();
    }
    
	@POST
    public Response create(@RequestBody Category Category) {
        Optional<Category> savedOp = service.create(Category);
        return savedOp.isPresent() ? Response.ok( savedOp.get() ).build() :
                                     Response.status( Response.Status.CONFLICT ).build();
    }

    @GET
    public Response getAll() {
        List<Category> items = service.getAll();
        return items.size() > 0 ? Response.ok( items ).build() :
                                  Response.status( Response.Status.NOT_FOUND ).build();
    }

    @GET
    @Path("{categoryId}")
    public Response getById(@PathParam("categoryId") Long categoryId) {
        Optional<Category> opt = service.findById(categoryId);
        return opt.isPresent() ? Response.ok( opt.get() ).build() :
                                 Response.status(Response.Status.NOT_FOUND).build() ;
    }

}