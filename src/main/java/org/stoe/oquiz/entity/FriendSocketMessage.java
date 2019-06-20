package org.stoe.oquiz.entity;

import com.google.gson.Gson;

public class FriendSocketMessage extends Message {
    private User data;

    // **************************************************
    // Constructors
    // **************************************************
    public FriendSocketMessage(String topic, String token, int from, int to, User data) {
        super(topic, "", token, from, to);
        setData(data);
    }
    
    public FriendSocketMessage(String topic, String fromAPI, String token, int from, int to, User data) {
        super(topic, fromAPI, token, from, to);
        setData(data);
    }

    // **************************************************
    // Getters and Setters
    // **************************************************
    public User getdata() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    // **************************************************
    // Public methods
    // **************************************************
    public static FriendSocketMessage decode(String s) {
        Gson g = new Gson();
        FriendSocketMessage result = g.fromJson(s, FriendSocketMessage.class);
        if (result.getFromAPI() == null) {
            result.setFromAPI("");
        }
        
        return result;
    }
}
