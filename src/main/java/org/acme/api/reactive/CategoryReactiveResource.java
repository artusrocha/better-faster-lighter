package org.acme.api.reactive;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.acme.entity.category.Category;
import org.acme.entity.category.CategoryReactiveService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/rx/category")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryReactiveResource {

	@Inject
    private CategoryReactiveService service;

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
    public Multi<Category> getAll() {
        Multi<Category> items = service.getAll();
        return items;// > 0 ? Response.ok( items ).build() :
                     //             Response.status( Response.Status.NOT_FOUND ).build();
    }

    @GET
    @Path("{categoryId}")
    public Uni<Category> getById(@PathParam("categoryId") Long categoryId) {
        return service.findById(categoryId);
        //return opt.isPresent() ? Response.ok( opt.get() ).build() :
        //                         Response.status(Response.Status.NOT_FOUND).build() ;
    }

}