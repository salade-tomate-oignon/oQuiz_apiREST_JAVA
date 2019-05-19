package org.stoe.oquiz.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.stoe.oquiz.entity.User;
import org.stoe.oquiz.service.implementation.UserInterfaceImpl;

/**
 * User resource (exposed at "user" path)
 */
@Path("user")
public class UserResource {

	/**
	 * Class Constructor
	 */
	public UserResource() {
		super();
	}

	@Path("sign-in")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signIn(User user) {
		UserInterfaceImpl service = new UserInterfaceImpl();

        return service.signIn(user);
	}
	
	@Path("log-in/pseudo/{pseudo}/password/{password}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response logIn(@PathParam("pseudo") String pseudo, @PathParam("password") String password) {
		UserInterfaceImpl service = new UserInterfaceImpl();

        return service.logIn(new User(pseudo, password));
    }

}
