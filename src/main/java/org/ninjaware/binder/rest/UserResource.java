package org.ninjaware.binder.rest;

import org.ninjware.binder.model.User;
import org.ninjware.binder.model.UserDTO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Optional;

import static java.util.Objects.isNull;

@RequestScoped
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id")String id) {
        User user = userService.deleteUser(id);
        if (isNull(user)) {
            return Response.noContent().build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") String id,
                             @QueryParam("includeDeleted") boolean includeDeleted) {
        Optional<User> userOpt = userService.findUserById(id, includeDeleted);
        if (userOpt.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.ok(userOpt.get()).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") String id, UserDTO dto) {
        Optional<User> userOpt = userService.findUserById(id, false);
        if (userOpt.isEmpty()) {
            return Response.noContent().build();
        }
        User updatedUser = userService.updateUser(id, dto);
        return Response.ok(updatedUser).build();
    }

    @POST
    public Response create(UserDTO dto) {
        User user = userService.createUser(dto);
        URI uri = URI.create("/user/" + user.getId());
        return Response.created(uri).build();
    }

}