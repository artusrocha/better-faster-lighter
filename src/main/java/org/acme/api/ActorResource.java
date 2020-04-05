package org.acme.api;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.entity.Actor;
import org.acme.entity.ActorService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/actor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActorResource {

	@Inject
    private ActorService service;

    @GET
	@Path("{id}")
    public Actor getById(@PathParam("id") String id) {
        Optional<Actor> actorOp = service.findById(id);
        return actorOp.isPresent() ? actorOp.get() : null;
	}

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") String id) {
        Boolean deleted =
                service.delete(id) ;
        return Response.noContent().build();
    }
    
	@POST
    public Actor create(@RequestBody Actor actor) {
        Optional<Actor> savedOp = service.create(actor);
        return savedOp.isPresent() ? savedOp.get() : null;
    }
	
	@POST
	@Path("/populate")
    public List<Actor> populate() {
        return service.populate() ;
    }
	
    @GET
    public Response getAllPosts() {
        return Response.ok( this.service.getAll() ).build();
    }
}