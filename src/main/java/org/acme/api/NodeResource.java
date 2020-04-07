package org.acme.api;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.entity.node.Node;
import org.acme.entity.node.NodeService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/node")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NodeResource {

	@Inject
    private NodeService service;

    @GET
	@Path("{id}")
    public Node getById(@PathParam("id") Long id) {
        Optional<Node> NodeOp = service.findById(id);
        return NodeOp.isPresent() ? NodeOp.get() : null;
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
    public Response getAllPosts() {
        return Response.ok( this.service.getAll() ).build();
    }
}