package org.stoe.oquiz.entity;

import java.util.regex.*;

public class User {
    // **************************************************
    // Fields
    // **************************************************
	private int id;
	private String firstName;
	private String lastName;
	private String pseudo;
	private String email;
	private String password;
	private String avatarName;

    // **************************************************
    // Constructors
    // **************************************************
	public User() {
		super();
	}

	public User(int id) {
		super();
		this.setId(id);
	}

	public User(String pseudo, String password) {
		super();
		this.setPseudo(pseudo);
		this.setPassword(password);
	}

	public User(int id, String firstName, String lastName, String pseudo, String email, String avatarName) {
		super();
		this.setId(id);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setPseudo(pseudo);
		this.setEmail(email);
		this.setAvatarName(avatarName);
	}

	public User(String firstName, String lastName, String pseudo, String email, String password) {
		super();
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setPseudo(pseudo);
		this.setEmail(email);
		this.setPassword(password);
	}

    // **************************************************
    // Getters and Setters
    // **************************************************
    /**
	 * @return the avatar
	 */
	public String getAvatarName() {
		return avatarName;
	}

    /**
	 * @param avatarName the avatar to set
	 */
	public void setAvatarName(String avatarName) {
		this.avatarName = avatarName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = (User.checkPassword(password))?password:"";
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = (User.checkEmail(email))?email:"";
	}

	/**
	 * @return the pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * @param pseudo the pseudo to set
	 */
	public void setPseudo(String pseudo) {
		this.pseudo = (User.checkPseudo(pseudo))?pseudo:"";
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = (User.checkLastName(lastName))?lastName:"";
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = (User.checkFirstName(firstName))?firstName:"";
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
    }
    
    // **************************************************
    // Public methods
    // **************************************************
	/**
     * Vérifie la validité d'un prénom
     * 
	 * @param firstName
	 * @return boolean
	 */
	public static boolean checkFirstName(String firstName) {
		Pattern p = Pattern.compile("^[a-zA-Z]{2,49}$");
		Matcher m = p.matcher(firstName); 
		
		return m.matches();
	}

	/**
     * Vérifie la validité d'un nom
     * 
	 * @param lastName
	 * @return boolean
	 */
	public static boolean checkLastName(String lastName) {
		Pattern p = Pattern.compile("^[a-zA-Z]{2}[a-zA-Z ]{0,47}$");
		Matcher m = p.matcher(lastName); 
		
		return m.matches();
	}

	/**
     * Vérifie la validité d'un pseudo
     * 
	 * @param pseudo
	 * @return boolean
	 */
	public static boolean checkPseudo(String pseudo) {
		Pattern p = Pattern.compile("^[a-zA-Z]{2,49}$");
		Matcher m = p.matcher(pseudo); 
		
		return m.matches();
	}

	/**
     * Vérifie la validité d'un email
     * 
	 * @param email
	 * @return boolean
	 */
	public static boolean checkEmail(String email) {
		Pattern p = Pattern.compile("^[a-zA-Z0-9._-]{2,}@[a-z0-9._-]{2,}.[a-z]{2,4}$");
		Matcher m = p.matcher(email); 
		
		return m.matches();
	}

	/**
     * Vérifie la validité d'un mot de passe
     * 
	 * @param password
	 * @return boolean
	 */
	public static boolean checkPassword(String password) {
		Pattern p = Pattern.compile("^[a-zA-z0-9]{8,49}$");
		Matcher m = p.matcher(password); 
		
		return m.matches();
	}

}
