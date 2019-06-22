package org.stoe.oquiz.service.implementation;

import java.util.ArrayList;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.stoe.oquiz.common.Bdd;
import org.stoe.oquiz.common.Error;
import org.stoe.oquiz.entity.FriendSocketMessage;
import org.stoe.oquiz.entity.User;
import org.stoe.oquiz.service.dao.FriendDAO;
import org.stoe.oquiz.service.dao.UserDAO;
import org.stoe.oquiz.service.functionality.FriendInterface;
import org.stoe.oquiz.websocket.client.FriendSocketClient;

public class FriendInterfaceImpl implements FriendInterface {
    private FriendSocketClient socket;

    // **************************************************
    // Constructors
    // **************************************************
	/**
	 * Default Constructor
	 */
	public FriendInterfaceImpl() {
        super();
        this.setSocket(new FriendSocketClient());
    }
    
    // **************************************************
    // Getters and Setters
    // **************************************************
    public FriendSocketClient getSocket() {
		return socket;
	}

	public void setSocket(FriendSocketClient socket) {
		this.socket = socket;
	}

    // **************************************************
    // Public methods
    // **************************************************
	@Override
	public Response friendRequest(int authorId, String pseudoFriend) {
        ResponseBuilder resp = null;
        ArrayList<Error> err = new ArrayList<Error>();
        
        // Accès à la table <user>
        UserDAO userDAO = new UserDAO(Bdd.getConnection());
        int friendId = userDAO.findIdFromPseudo(pseudoFriend);

        if (friendId == 0) {
            resp = Response.status(Response.Status.BAD_REQUEST);
            err.add(new Error(1, "This pseudo doesn't exist"));
            return resp.entity(err).build();
        } 

        if (authorId == friendId) {
            resp = Response.status(Response.Status.UNAUTHORIZED);
            err.add(new Error(2, "forbidden request"));
            return resp.entity(err).build();
        }

        // Accès à la table <friend>
        FriendDAO friendDAO = new FriendDAO(Bdd.getConnection());

        switch (friendDAO.getStatus(authorId, friendId)) {
            case 1:
                // <authorId> et <friendId> sont déjà amis
                resp = Response.status(Response.Status.BAD_REQUEST);
                err.add(new Error(3, "you are already friends"));
                break;
            case 0:
                // <authorId> renvoie une demande d'ami
                if (friendDAO.updateDate(authorId, friendId)) {
                    FriendSocketMessage msg = new FriendSocketMessage("on hold", "API's token", "", authorId, friendId, this.findFriendDataFromId(authorId));
                    this.socket.send(msg);
                    resp = Response.status(Response.Status.OK);
                } else {
                    resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    err.add(new Error(4, "internal server error"));
                }
                break;
            case -1:
                // <authorId> renvoie une demande d'ami après un refus de <friendId>
                if (friendDAO.updateStatus(authorId, friendId, 0)) {
                    FriendSocketMessage msg = new FriendSocketMessage("on hold", "API's token", "", authorId, friendId, this.findFriendDataFromId(authorId));
                    this.socket.send(msg);
                    resp = Response.status(Response.Status.OK);
                } else {
                    resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    err.add(new Error(4, "internal server error"));
                }
                break;
            case -2:
                // <friendId> a bloqué <authorId> 
                resp = Response.status(Response.Status.BAD_REQUEST);
                err.add(new Error(5, "you have been blocked"));
                break;

            case -3:
                if (friendDAO.add(authorId, friendId, 0)) {
                    FriendSocketMessage msg = new FriendSocketMessage("on hold", "API's token", "", authorId, friendId, this.findFriendDataFromId(authorId));
                    this.socket.send(msg);
                    resp = Response.status(Response.Status.CREATED);
                } else {
                    resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    err.add(new Error(4, "internal server error"));
                }
                break;

            default:
                resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                err.add(new Error(4, "internal server error"));
                break;
        }

        return resp.entity(err).build();
	}

    @Override
    public Response getAllfriendRequests(int userId) {
        ResponseBuilder resp = null;
        ArrayList<Error> err = new ArrayList<Error>();
        ArrayList<User> result = new ArrayList<User>();

        // Accès à la table <friend>
        FriendDAO friendDAO = new FriendDAO(Bdd.getConnection());
        result = friendDAO.getAllfriendRequests(userId);

        // Il n'y a pas d'erreur
        if (result != null) {
            return Response.status(Response.Status.OK).entity(result).build();
        }

        resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        err.add(new Error(1, "internal server error"));
        
        return resp.entity(err).build();
    }
    
    @Override
    public Response getAllBlockedfriends(int userId) {
        return getFriendsFromStatus(userId, -2);
    }
    
    @Override
    public Response getAllfriends(int userId) {
        return getFriendsFromStatus(userId, 1);
    }
    
    @Override
    public Response blockUser(int userId, int otherUserId) {

        // Accès à la table <friend>
        FriendDAO friendDAO = new FriendDAO(Bdd.getConnection());

        // Relation <otherUserId> ==> <userId>
        int status = friendDAO.getStatus(otherUserId, userId);
        if(status == -3) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if(status == -4) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
        if(!friendDAO.updateStatus(otherUserId, userId, -2)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
        // Relation <userId> ==> <otherUserId>
        status = friendDAO.getStatus(userId, otherUserId);
        if(status >= -2) {
            if(!friendDAO.deleteRow(userId, otherUserId)) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
        if(status == -4) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.OK).build();
    }
    
    @Override
    public Response unBlockUser(int userId, int otherUserId) {

        // Accès à la table <friend>
        FriendDAO friendDAO = new FriendDAO(Bdd.getConnection());
        
        int status = friendDAO.getStatus(otherUserId, userId);
        if(status >= -2) {
            if(!friendDAO.deleteRow(otherUserId, userId)) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
        if(status == -4) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.OK).build();
    }
    
    @Override
    public Response removeFriend(int userId, int otherUserId) {

        // Accès à la table <friend>
        FriendDAO friendDAO = new FriendDAO(Bdd.getConnection());
        
        int status = friendDAO.getStatus(userId, otherUserId);
        if(status == -4) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if(status < 1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if(!friendDAO.deleteRow(otherUserId, userId)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if(!friendDAO.deleteRow(userId, otherUserId)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.OK).build();
    }
    
    @Override
    public Response declineFriendRequest(int userId, int otherUserId) {

        // Accès à la table <friend>
        FriendDAO friendDAO = new FriendDAO(Bdd.getConnection());
        
        int status = friendDAO.getStatus(otherUserId, userId);
        if(status == 0) {
            if(!friendDAO.updateStatus(otherUserId, userId, -1)) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
        else if(status == -4) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.OK).build();
    }
    
    @Override
    public Response acceptFriendRequest(int userId, int otherUserId) {

        // Accès à la table <friend>
        FriendDAO friendDAO = new FriendDAO(Bdd.getConnection());
        
        int status = friendDAO.getStatus(otherUserId, userId);
        if(status == 0) {
            int stts = friendDAO.getStatus(userId, otherUserId);

            if(stts == -4) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            else if(stts == -3) {
                if(!friendDAO.add(userId, otherUserId, 1))
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            else {
                if(!friendDAO.updateStatus(userId, otherUserId, 1))
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            
            if(!friendDAO.updateStatus(otherUserId, userId, 1)) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
        else if(status == -4) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    // **************************************************
    // Private methods
    // **************************************************
    private Response getFriendsFromStatus(int userId, int status) {
        ResponseBuilder resp = null;
        ArrayList<Error> err = new ArrayList<Error>();
        ArrayList<User> result = new ArrayList<User>();

        // Accès à la table <friend>
        FriendDAO friendDAO = new FriendDAO(Bdd.getConnection());
        result = friendDAO.getFriendsFromStatus(userId, status);

        // Il n'y a pas d'erreur
        if (result != null) {
            return Response.status(Response.Status.OK).entity(result).build();
        }

        resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        err.add(new Error(1, "internal server error"));
        
        return resp.entity(err).build();
    }

    private User findFriendDataFromId(int friendId) {
        UserDAO userDAO = new UserDAO(Bdd.getConnection());
        User data = userDAO.find(friendId);

        return new User(data.getId(), data.getPseudo());
    }

}
