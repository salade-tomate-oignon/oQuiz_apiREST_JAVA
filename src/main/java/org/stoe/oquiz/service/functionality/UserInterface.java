package org.stoe.oquiz.service.functionality;

import javax.ws.rs.core.Response;

import org.stoe.oquiz.entity.User;

public interface UserInterface {
    
    /**
     * Permet l'inscription d'un utilisateur
     * 
     * @param user
     * @return
     */
    public Response signUp(User user);
    
    /**
     * Permet à un utilisateur de se connecter
     * 
     * @param user
     * @return
     */
    public Response logIn(User user);
    
    /**
     * Permet à un utilisateur de mettre à jour son profil
     * 
     * @param user
     * @param id
     * @return
     */
    public Response update(User user, int id);
}
