package org.stoe.oquiz.service.functionality;

import javax.ws.rs.core.Response;

import org.stoe.oquiz.entity.User;

public interface UserInterface {
    
    public Response signIn(User user);
    
    public Response logIn(User user);
}
