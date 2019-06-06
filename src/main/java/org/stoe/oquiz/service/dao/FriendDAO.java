package org.stoe.oquiz.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.stoe.oquiz.entity.User;

public class FriendDAO extends DAO<User> {
	// **************************************************
    // Constructors
    // **************************************************
	public FriendDAO(Connection connect) {
		super(connect);
	}

    // **************************************************
    // Public methods
    // **************************************************
    @Override
    public int create(User obj) {
        return 0;
    }

    @Override
    public boolean delete(User obj) {
        return false;
    }

    @Override
    public int update(User obj) {
        return 0;
    }

    @Override
    public User find(int id) {
        return null;
    }

    /**
     * Met à jour la date d'envoi d'une demande d'ami
     * 
     * @param userId
     * @param friendId
     * @return
     */
    public boolean updateDate(int userId, int friendId) {
        if (this.connect == null) 
			return false;

		try {
			String query = "UPDATE friend SET date=NOW() WHERE user_id=? AND friend_id=?";
			PreparedStatement preparedStmt = this.connect.prepareStatement(query);
			
			preparedStmt.setLong(1, userId);
			preparedStmt.setLong (2, friendId);
			preparedStmt.execute();
			preparedStmt.close();
		} catch (SQLException e) {
			System.out.println("service.dao.FriendDAO.updateDate(): " + e.getMessage());
			return false;
		}
		
		return true;
    }

    /**
     * Ajoute une relation dans la table <ami>
     * 
     * @param userId
     * @param friendId
     * @return
     */
    public boolean add(int userId, int friendId) {
        if (this.connect == null) 
			return false;

		try {
			String query = "INSERT INTO friend (user_id, friend_id, status, date) VALUES(?, ?, 0, NOW())";
			PreparedStatement preparedStmt = this.connect.prepareStatement(query);
			
			preparedStmt.setLong(1, userId);
			preparedStmt.setLong (2, friendId);
			preparedStmt.execute();
			preparedStmt.close();
		} catch (SQLException e) {
			System.out.println("service.dao.FriendDAO.friendRequest(): " + e.getMessage());
			return false;
		}
		
		return true;
    }
    
    /**
     * Retourne le statut 
     * 
     * @param userId
     * @param friendId
     * @return
     */
    public int getStatus(int userId, int friendId) {
        String query = "SELECT status FROM friend WHERE user_id = ? AND friend_id = ?";
		PreparedStatement preparedStmt;
        ResultSet result;
        int res = -3;
        
        try {
			preparedStmt = this.connect.prepareStatement(query);
			preparedStmt.setLong(1, userId);
			preparedStmt.setLong (2, friendId);
			result = preparedStmt.executeQuery();
			
			if (result.next()) {
				res = result.getInt("status");
			} else {
				res = -3;
			}
			
			preparedStmt.close();
			result.close();
		} catch (SQLException e) {
			System.out.println("service.dao.FriendDAO.getStatus(): " + e.getMessage());
        }
        
        return res;
    }

}
