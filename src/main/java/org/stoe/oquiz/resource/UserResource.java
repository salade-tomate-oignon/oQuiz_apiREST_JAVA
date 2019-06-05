package org.stoe.oquiz.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    // **************************************************
    // Constructors
    // **************************************************
	/**
	 * Default Constructor
	 */
	public UserResource() {
		super();
	}

    // **************************************************
    // Public methods
    // **************************************************
    /**
	 * Inscription d'un utilisateur
	 */
	@Path("sign-up")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signIn(User user) {
		UserInterfaceImpl service = new UserInterfaceImpl();

        return service.signUp(user);
	}
    
    /**
	 * Connexion d'un utilisateur
	 */
	@Path("log-in/pseudo/{pseudo}/password/{password}")
	@GET
	public Response logIn(@PathParam("pseudo") String pseudo, @PathParam("password") String password) {
		UserInterfaceImpl service = new UserInterfaceImpl();

        return service.logIn(new User(pseudo, password));
    }

    /**
	 * Mise-à-jour du profil d'un utilisateur
	 */
    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, User user) {
		UserInterfaceImpl service = new UserInterfaceImpl();

        return service.update(user, id);
    }

}
