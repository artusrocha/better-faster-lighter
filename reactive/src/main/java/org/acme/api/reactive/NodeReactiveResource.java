package org.acme.api.reactive;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.acme.entity.node.Node;
import org.acme.entity.node.NodeReactiveService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/rx/node")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NodeReactiveResource {

	@Inject
    private NodeReactiveService service;

    @GET
	@Path("{id}")
    public Uni<Node> getById(@PathParam("id") Long id) {
        return service.findById(id);
        //return NodeOp.isPresent() ? NodeOp.get() : null;
	}

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") Long id) {
        Boolean deleted =
                service.delete(id) ;
        return Response.noContent().build();
    }
    
	@POST
    public Node create(@RequestBody Node Node) {
        Optional<Node> savedOp = service.create(Node);
        return savedOp.isPresent() ? savedOp.get() : null;
    }
	
	@POST
	@Path("/populate")
    public List<Node> populate() {
        return service.populate() ;
    }
	
    @GET
    public Multi<Node> getAllPosts() {
        return service.getAll();
        //return Response.ok( this.service.getAll() ).build();
    }
}