package org.acme.api;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class API <I, T> { // <IdType, Type>

    abstract Optional<T> srvFindById(I id);
    protected abstract Boolean srvDelete(I id);
    protected abstract Optional<T> srvCreate(T entity);
    protected abstract List<T> srvGetAll();

    @GET
	@Path("{id}")
    public T getById(@PathParam("id") I id) {
        Optional<T> opt = srvFindById(id);
        return opt.isPresent() ? opt.get() : null;
	}

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") I id) {
        Boolean deleted =
                srvDelete(id) ;
        return Response.noContent().build();
    }

    @POST
    public T create(@RequestBody T entity) {
        Optional<T> savedOp = srvCreate(entity);
        return savedOp.isPresent() ? savedOp.get() : null;
    }

    @GET
    @Path("/all")
    public Response getAll() {
        return Response.ok( srvGetAll() ).build();
    }

}