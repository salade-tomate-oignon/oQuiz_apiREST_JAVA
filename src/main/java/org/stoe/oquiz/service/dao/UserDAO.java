package org.stoe.oquiz.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import org.stoe.oquiz.entity.User;

public class UserDAO extends DAO<User> {

	public UserDAO(Connection connect) {
		super(connect);
	}

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
	public boolean update(User obj) {
		return false;
	}

	@Override
	public User find(int id) {
		return null;
	}

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

}
