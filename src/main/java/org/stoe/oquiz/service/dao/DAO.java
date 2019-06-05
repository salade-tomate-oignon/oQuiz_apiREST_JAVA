package org.stoe.oquiz.service.dao;

import java.sql.Connection;

public abstract class DAO<T> {
    protected Connection connect = null;
   
    public DAO(Connection connect){
        this.connect = connect;
    }
    
    /**
     * Méthode de création
    * @param obj
    * @return boolean 
    */
    public abstract int create(T obj);

    /**
     * Méthode pour effacer
    * @param obj
    * @return boolean 
    */
    public abstract boolean delete(T obj);

    /**
     * Méthode de mise à jour
    * @param obj
    * @return boolean
    */
    public abstract int update(T obj);

    /**
     * Méthode de recherche des informations
    * @param id
    * @return T
    */
    public abstract T find(int id);
}
