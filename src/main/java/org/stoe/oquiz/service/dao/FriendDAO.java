package org.stoe.oquiz.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
     * Supprime une ligne de la table <friend>
     * 
     * @param userId
     * @param friendId
     * @return
     */
    public boolean deleteRow(int userId, int friendId) {
        if (this.connect == null) 
			return false;

		try {
			String query = "DELETE FROM friend WHERE user_id=? AND friend_id=?";
			PreparedStatement preparedStmt = this.connect.prepareStatement(query);
			
			preparedStmt.setLong(1, userId);
			preparedStmt.setLong(2, friendId);
			preparedStmt.execute();
			preparedStmt.close();
		} catch (SQLException e) {
			System.out.println("service.dao.FriendDAO.deleteRow(): " + e.getMessage());
			return false;
		}
		
		return true;
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
			preparedStmt.setLong(2, friendId);
			preparedStmt.execute();
			preparedStmt.close();
		} catch (SQLException e) {
			System.out.println("service.dao.FriendDAO.updateDate(): " + e.getMessage());
			return false;
		}
		
		return true;
    }
    
    /**
     * Met à jour le statut de la relation entre <userId> et <friendId>
     * 
     * @param userId
     * @param friendId
     * @param status
     * @return
     */
    public boolean updateStatus(int userId, int friendId, int status) {
        if (this.connect == null) 
			return false;

		try {
			String query = "UPDATE friend SET status=?, date=NOW() WHERE user_id=? AND friend_id=?";
			PreparedStatement preparedStmt = this.connect.prepareStatement(query);
			
			preparedStmt.setLong(1, status);
			preparedStmt.setLong(2, userId);
			preparedStmt.setLong(3, friendId);
			preparedStmt.execute();
			preparedStmt.close();
		} catch (SQLException e) {
			System.out.println("service.dao.FriendDAO.updateStatus(): " + e.getMessage());
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
			preparedStmt.setLong(2, friendId);
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
        int res = -4;
        
        try {
			preparedStmt = this.connect.prepareStatement(query);
			preparedStmt.setLong(1, userId);
			preparedStmt.setLong(2, friendId);
			result = preparedStmt.executeQuery();
			
			if (result.next()) {
				res = result.getInt("status");
			} else {
				res = -3;
			}
			
			preparedStmt.close();
			result.close();
		} catch (Exception e) {
			System.out.println("service.dao.FriendDAO.getStatus(): " + e.getMessage());
        }
        
        return res;
    }
    
    /**
     * Retourne une liste d'amis de <userId> suivant leur statut <status>
     * 
     * @param userId
     * @param status
     * @return
     */
    public  ArrayList<User> getFriendsFromStatus(int userId, int status) {
        String query = "SELECT user.* FROM friend JOIN user ON friend.friend_id = user.id WHERE friend.user_id = ? AND friend.status = ?";
		PreparedStatement preparedStmt;
        ResultSet result;
        ArrayList<User> res = new ArrayList<User>();
        
        try {
			preparedStmt = this.connect.prepareStatement(query);
			preparedStmt.setLong(1, userId);
			preparedStmt.setLong(2, status);
			result = preparedStmt.executeQuery();
			
			while (result.next()) {
                User user = new User(result.getInt("id"), result.getString("pseudo"));
				res.add(user);
            } 
			
			preparedStmt.close();
			result.close();
		} catch (SQLException e) {
            result = null;
			System.out.println("service.dao.FriendDAO.getFriendsFromStatus(): " + e.getMessage());
        }
        
        return res;
    }
    
    /**
     * Retourne la liste de tous les utilisateurs ayant fait une demande d'ami à <userId> 
     * 
     * @param userId
     * @return
     */
    public  ArrayList<User> getAllfriendRequests(int userId) {
        String query = "SELECT user.* FROM friend JOIN user ON friend.user_id = user.id WHERE friend.friend_id = ? AND friend.status = 0 ORDER BY friend.date DESC";
		PreparedStatement preparedStmt;
        ResultSet result;
        ArrayList<User> res = new ArrayList<User>();
        
        try {
			preparedStmt = this.connect.prepareStatement(query);
			preparedStmt.setLong(1, userId);
			result = preparedStmt.executeQuery();
			
			while (result.next()) {
				User user = new User(result.getInt("id"), result.getString("pseudo"));
				res.add(user);
            } 
			
			preparedStmt.close();
			result.close();
		} catch (SQLException e) {
            result = null;
			System.out.println("service.dao.FriendDAO.getFriendsFromStatus(): " + e.getMessage());
        }
        
        return res;
    }

}
