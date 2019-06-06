package org.stoe.oquiz.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import org.stoe.oquiz.entity.User;

public class UserDAO extends DAO<User> {
    // **************************************************
    // Constructors
    // **************************************************
	public UserDAO(Connection connect) {
		super(connect);
	}

    // **************************************************
    // Public methods
    // **************************************************
	@Override
	public int create(User obj) {
		if (this.connect == null) 
			return 1;

		try {
			String query = "INSERT INTO user (first_name, last_name, pseudo, email, password) VALUES(?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = this.connect.prepareStatement(query);
			
			preparedStmt.setString (1, obj.getFirstName());
			preparedStmt.setString (2, obj.getLastName());
			preparedStmt.setString (3, obj.getPseudo());
			preparedStmt.setString (4, obj.getEmail());
			preparedStmt.setString (5, obj.getPassword());
			preparedStmt.execute();
			preparedStmt.close();
		} catch (SQLException e) {
			System.out.println("service.dao.UserDAO.create(): " + e.getMessage());
			if (Pattern.compile("pseudo").matcher(e.getMessage()).find()) 
				return 2;
			if (Pattern.compile("email").matcher(e.getMessage()).find()) 
				return 3;
		}
		
		return 0;
	}

	@Override
	public boolean delete(User obj) {
		return false;
	}

	@Override
	public int update(User obj) {
        int res = 0;

        if (this.connect == null) 
            return 1;

        if (obj.getPassword().isEmpty()) 
            res = this.updateWithoutPassword(obj);
        else 
            res = this.updateAll(obj);
        
		return res;
	}

	@Override
	public User find(int id) {
		return null;
    }

	/**
     * Trouve les informations d'un utilisateur à partir de son pseudo 
     * et de son mot de passe
     * 
	 * @param obj
	 * @return
	 */
	public User findUserDataFromPseudoPassword(User obj) {
		User res = null;
		String query = "SELECT * FROM user WHERE pseudo = ? AND password = ?";
		PreparedStatement preparedStmt;
		ResultSet result;

		if (this.connect == null) 
			return res;
		
		try {
			preparedStmt = this.connect.prepareStatement(query);
			preparedStmt.setString (1, obj.getPseudo());
			preparedStmt.setString (2, obj.getPassword());
			result = preparedStmt.executeQuery();
			
			if (result.next()) {
				res = new User(result.getInt("id"), result.getString("first_name"), result.getString("last_name"),
					result.getString("pseudo"), result.getString("email"), result.getString("avatar_name"));
			} else {
				res = new User(0);
			}
			
			preparedStmt.close();
			result.close();
		} catch (SQLException e) {
			System.out.println("service.dao.UserDAO.findUserDataFromPseudoPassword(): " + e.getMessage());
		}

		return res;
    }

    public int findIdFromPseudo(String pseudoFriend) {
        int res = 0;
		String query = "SELECT id FROM user WHERE pseudo = ?";
		PreparedStatement preparedStmt;
		ResultSet result;
		
		try {
			preparedStmt = this.connect.prepareStatement(query);
			preparedStmt.setString (1, pseudoFriend);
			result = preparedStmt.executeQuery();
			
			if (result.next()){
				res = result.getInt("id");
            }
			
			preparedStmt.close();
			result.close();
		} catch (SQLException e) {
			System.out.println("service.dao.UserDAO.findIdFromPseudo(): " + e.getMessage());
		}

		return res;
    }
    
    // **************************************************
    // Private methods
    // **************************************************
    private int updateAll(User obj) {
        
        try {
            String query = "UPDATE user SET first_name=?, last_name=?, pseudo=?, email=?, password=? WHERE id=?";
            PreparedStatement preparedStmt = this.connect.prepareStatement(query);
            
            preparedStmt.setString (1, obj.getFirstName());
            preparedStmt.setString (2, obj.getLastName());
            preparedStmt.setString (3, obj.getPseudo());
            preparedStmt.setString (4, obj.getEmail());
            preparedStmt.setString (5, obj.getPassword());
            preparedStmt.setLong(6, obj.getId());
            preparedStmt.execute();
            preparedStmt.close();
        } catch (SQLException e) {
            System.out.println("service.dao.UserDAO.updateAll(): " + e.getMessage());
            if (Pattern.compile("pseudo").matcher(e.getMessage()).find()) 
                return 2;
            if (Pattern.compile("email").matcher(e.getMessage()).find()) 
                return 3;
        }

        return 0;
    }
    
    private int updateWithoutPassword(User obj) {

        try {
            String query = "UPDATE user SET first_name=?, last_name=?, pseudo=?, email=? WHERE id=?";
            PreparedStatement preparedStmt = this.connect.prepareStatement(query);
            
            preparedStmt.setString (1, obj.getFirstName());
            preparedStmt.setString (2, obj.getLastName());
            preparedStmt.setString (3, obj.getPseudo());
            preparedStmt.setString (4, obj.getEmail());
            preparedStmt.setLong(5, obj.getId());
            preparedStmt.execute();
            preparedStmt.close();
        } catch (SQLException e) {
            System.out.println("service.dao.UserDAO.updateWithoutPassword(): " + e.getMessage());
            if (Pattern.compile("pseudo").matcher(e.getMessage()).find()) 
                return 2;
            if (Pattern.compile("email").matcher(e.getMessage()).find()) 
                return 3;
        }

        return 0;
    }

}
