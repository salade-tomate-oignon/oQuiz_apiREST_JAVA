package org.stoe.oquiz.common;

import java.sql.Connection;
import java.sql.DriverManager;

public class Bdd {
    // **************************************************
    // Constants
    // **************************************************
    private static final String urlBDD = "jdbc:mysql://localhost:3306/org.stoe.oquiz";
    private static final String userBDD = "root";
    private static final String passwordBDD = "root";
    
    // **************************************************
    // Constructors
    // **************************************************
    public Bdd() {
    }
    
    // **************************************************
    // Public methods
    // **************************************************
    /**
     * Permet de se connecter à la base de données
     * 
     * @return Connection
     */
    public static Connection getConnection() {
        Connection bdd = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            bdd = DriverManager.getConnection(Bdd.urlBDD, Bdd.userBDD, Bdd.passwordBDD);
		} catch (Exception e) {
			System.out.print("common.Bdd.getConnection(): "); 
			System.out.println(e); 
		}
        
        return bdd;
    }
}
