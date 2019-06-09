package org.stoe.oquiz.service.implementation;

import java.util.ArrayList;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.stoe.oquiz.common.Bdd;
import org.stoe.oquiz.common.Error;
import org.stoe.oquiz.entity.User;
import org.stoe.oquiz.service.dao.FriendDAO;
import org.stoe.oquiz.service.dao.UserDAO;
import org.stoe.oquiz.service.functionality.FriendInterface;

public class FriendInterfaceImpl implements FriendInterface {

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
                    resp = Response.status(Response.Status.OK);
                } else {
                    resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    err.add(new Error(4, "internal server error"));
                }
                break;
            case -1:
                // <authorId> renvoie une demande d'ami après un refus de <friendId>
                if (friendDAO.updateDate(authorId, friendId)) {
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
            default:
                if (friendDAO.add(authorId, friendId)) {
                    resp = Response.status(Response.Status.CREATED);
                } else {
                    resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    err.add(new Error(4, "internal server error"));
                }
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
        result = friendDAO.getFriendsFromStatus(userId, 0);

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
        ResponseBuilder resp = null;
        ArrayList<Error> err = new ArrayList<Error>();
        ArrayList<User> result = new ArrayList<User>();

        // Accès à la table <friend>
        FriendDAO friendDAO = new FriendDAO(Bdd.getConnection());
        result = friendDAO.getFriendsFromStatus(userId, -2);

        // Il n'y a pas d'erreur
        if (result != null) {
            return Response.status(Response.Status.OK).entity(result).build();
        }

        resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        err.add(new Error(1, "internal server error"));
        
        return resp.entity(err).build();
    }
    
    @Override
    public Response getAllfriends(int userId) {
        ResponseBuilder resp = null;
        ArrayList<Error> err = new ArrayList<Error>();
        ArrayList<User> result = new ArrayList<User>();

        // Accès à la table <friend>
        FriendDAO friendDAO = new FriendDAO(Bdd.getConnection());
        result = friendDAO.getFriendsFromStatus(userId, 1);

        // Il n'y a pas d'erreur
        if (result != null) {
            return Response.status(Response.Status.OK).entity(result).build();
        }

        resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        err.add(new Error(1, "internal server error"));
        
        return resp.entity(err).build();
    }

}
