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
//@RequestScoped
public class ActorResource {

	@Inject
    private ActorService service;

    @GET
	@Path("{shortname}")
    @Produces({MediaType.APPLICATION_JSON})
    public Actor getByShortname(@PathParam("shortname") String shortname) {
        Optional<Actor> actorOp = service.findByShortname(shortname);
        return actorOp.isPresent() ? actorOp.get() : null;
	}
    
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Actor create(@RequestBody Actor actor) {
        Optional<Actor> savedOp = service.create(actor);//.get() ;
        return savedOp.isPresent() ? savedOp.get() : null;
    }
	
	@POST
	@Path("/populate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Actor> populate() {
        return service.populate() ;
    }
	
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPosts() {
        return Response.ok( this.service.getAll() ).build();
    }
}