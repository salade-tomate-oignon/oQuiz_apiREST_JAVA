package org.stoe.oquiz.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.stoe.oquiz.service.implementation.FriendInterfaceImpl;

/**
 * Friend resource (exposed at "friend" path)
 */
@Path("friend")
@Produces(MediaType.APPLICATION_JSON)
public class FriendResource {
    // **************************************************
    // Constructors
    // **************************************************
	/**
	 * Default Constructor
	 */
	public FriendResource() {
        super();
	}

	// **************************************************
    // Public methods
    // **************************************************
    /**
     * Envoie une demande d'ami de <authorID> à <pseudoFriend>
     * 
     * @param authorID
     * @param pseudoFriend
     * @return
     */
    @Path("user/{authorID}/friend-request/friend/{pseudoFriend}")
    @POST
    public Response friendRequest(@PathParam("authorID") int authorID, @PathParam("pseudoFriend") String pseudoFriend) {
        FriendInterfaceImpl service = new FriendInterfaceImpl();

        return service.friendRequest(authorID, pseudoFriend);
    }
    
    /**
     * Retourne la liste de tous les utilisateurs ayant fait une demande d'ami à <userId> 
     * 
     * @param userId
     * @return
     */
    @Path("user/{userId}/getAllfriendRequests")
    @GET
    public Response getAllfriendRequests(@PathParam("userId") int userId) {
        FriendInterfaceImpl service = new FriendInterfaceImpl();

        return service.getAllfriendRequests(userId);
    }

    /**
     * Retourne la liste de tous les utilisateurs bloqués par <userId> 
     * 
     * @param userId
     * @return
     */
    @Path("user/{userId}/getAllBlockedfriends")
    @GET
    public Response getAllBlockedfriends(@PathParam("userId") int userId) {
        FriendInterfaceImpl service = new FriendInterfaceImpl();

        return service.getAllBlockedfriends(userId);
    }
    
    /**
     * Retourne la liste de tous les amis de <userId> 
     * 
     * @param userId
     * @return
     */
    @Path("user/{userId}/getAllfriends")
    @GET
    public Response getAllfriends(@PathParam("userId") int userId) {
        FriendInterfaceImpl service = new FriendInterfaceImpl();

        return service.getAllfriends(userId);
    }
    
    /**
     * <userId> bloque <otherUserId>
     * 
     * @param userId
     * @param otherUserId
     * @return
     */
    @Path("user/{userId}/blockUser/otherUser/{otherUserId}")
    @PUT
    public Response blockUser(@PathParam("userId") int userId, @PathParam("otherUserId") int otherUserId) {
        FriendInterfaceImpl service = new FriendInterfaceImpl();

        return service.blockUser(userId, otherUserId);
    }

    /**
     * <userId> débloque <otherUserId>
     * 
     * @param userId
     * @param otherUserId
     * @return
     */
    @Path("user/{userId}/unBlockUser/otherUser/{otherUserId}")
    @DELETE
    public Response unBlockUser(@PathParam("userId") int userId, @PathParam("otherUserId") int otherUserId) {
        FriendInterfaceImpl service = new FriendInterfaceImpl();

        return service.unBlockUser(userId, otherUserId);
    }
    
    /**
     * Retire un ami
     * 
     * @param userId
     * @param otherUserId
     * @return
     */
    @Path("user/{userId}/removeFriend/otherUser/{otherUserId}")
    @DELETE
    public Response removeFriend(@PathParam("userId") int userId, @PathParam("otherUserId") int otherUserId) {
        FriendInterfaceImpl service = new FriendInterfaceImpl();

        return service.removeFriend(userId, otherUserId);
    }
    
    /**
     * <userId> refuse la demande d'ami de <otherUserId>
     * 
     * @param userId
     * @param otherUserId
     * @return
     */
    @Path("user/{userId}/declineFriendRequest/otherUser/{otherUserId}")
    @PUT
    public Response declineFriendRequest(@PathParam("userId") int userId, @PathParam("otherUserId") int otherUserId) {
        FriendInterfaceImpl service = new FriendInterfaceImpl();

        return service.declineFriendRequest(userId, otherUserId);
    }
    
    /**
     * <userId> accepte la demande d'ami de <otherUserId>
     * 
     * @param userId
     * @param otherUserId
     * @return
     */
    @Path("user/{userId}/acceptFriendRequest/otherUser/{otherUserId}")
    @PUT
    public Response acceptFriendRequest(@PathParam("userId") int userId, @PathParam("otherUserId") int otherUserId) {
        FriendInterfaceImpl service = new FriendInterfaceImpl();

        return service.acceptFriendRequest(userId, otherUserId);
    }
}
