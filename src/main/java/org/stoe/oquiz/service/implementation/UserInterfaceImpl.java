package org.stoe.oquiz.service.implementation;

import java.util.ArrayList;

import javax.swing.text.html.parser.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.stoe.oquiz.common.Bdd;
import org.stoe.oquiz.common.Error;
import org.stoe.oquiz.entity.User;
import org.stoe.oquiz.service.dao.UserDAO;
import org.stoe.oquiz.service.functionality.UserInterface;

public class UserInterfaceImpl implements UserInterface {

    @Override
    public Response signIn(User user) {
        ResponseBuilder resp = null;
        ArrayList<Error> err = new ArrayList<Error>();

        // On contrôle les données
        if (user.getFirstName().isEmpty()) {
            resp = Response.status(Response.Status.BAD_REQUEST);
            err.add(new Error(1, "check the first name"));
        }

        if (user.getLastName().isEmpty()) {
            resp = Response.status(Response.Status.BAD_REQUEST);
            err.add(new Error(2, "check the last name"));
        }

        if (user.getPseudo().isEmpty()) {
            resp = Response.status(Response.Status.BAD_REQUEST);
            err.add(new Error(3, "check the pseudo"));
        }
        
        if (user.getEmail().isEmpty()) {
            resp = Response.status(Response.Status.BAD_REQUEST);
            err.add(new Error(4, "check the email"));
        }
        
        if (user.getPassword().isEmpty()) {
            resp = Response.status(Response.Status.BAD_REQUEST);
            err.add(new Error(5, "check the password"));
        }

        if (err.isEmpty()) {
            // Accès à la base de données
            UserDAO dao = new UserDAO(Bdd.getConnection());

            switch (dao.create(user)) {
                case 0:
                    resp = Response.status(Response.Status.OK);
                    break; 
                case 1:
                    resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    err.add(new Error(6, "internal server error"));
                    break;
                case 2:
                    resp = Response.status(Response.Status.BAD_REQUEST);
                    err.add(new Error(7, "this pseudo is unavailable"));
                    break;
                default:
                    resp = Response.status(Response.Status.BAD_REQUEST);
                    err.add(new Error(8, "this email is unavailable"));
                    break;
            }
        }
        
        return resp.entity(err).build();
    }

	@Override
	public Response logIn(User obj) {
        ResponseBuilder resp = null;
        User user;
        ArrayList<Error> err = new ArrayList<Error>();

        // On contrôle les données
        if (obj.getPseudo().isEmpty()) {
            resp = Response.status(Response.Status.BAD_REQUEST);
            err.add(new Error(1, "check the pseudo"));
        }

        if (obj.getPassword().isEmpty()) {
            resp = Response.status(Response.Status.BAD_REQUEST);
            err.add(new Error(2, "check the password"));
        }

        if (err.isEmpty()) {
            // Accès à la base de données
            UserDAO dao = new UserDAO(Bdd.getConnection());
            user = dao.findUserDataFromPseudoPassword(obj);

            if (user == null) {
                resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                err.add(new Error(3, "internal server error"));
            } else if (user.getId() == 0) {
                resp = Response.status(Response.Status.UNAUTHORIZED);
                err.add(new Error(4, "check the pseudo or password"));
            } else {
                resp = Response.status(Response.Status.OK);
                return resp.entity(user).build();
            }
        } 
        
        return resp.entity(err).build();
	}

}
