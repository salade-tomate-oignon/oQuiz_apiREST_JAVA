package org.stoe.oquiz.entity;

import com.google.gson.Gson;

public class Message {
    // **************************************************
    // Fields
    // **************************************************
    protected String topic;
    protected String fromAPI;
    protected String token;
    protected int from;
    protected int to;

    // **************************************************
    // Constructors
    // **************************************************
    public Message(String topic, String fromAPI, String token, int from, int to) {
        this.setTopic(topic);
        this.setFromAPI(fromAPI);
        this.setToken(token);
        this.setFrom(from);
        this.setTo(to);
    }
    
    // **************************************************
    // Getters and Setters
    // **************************************************
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getFromAPI() {
        return fromAPI;
    }

    public void setFromAPI(String fromAPI) {
        this.fromAPI = fromAPI;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    // **************************************************
    // Public methods
    // **************************************************
    public String encode() {
        Gson g = new Gson();
        return g.toJson(this);
    }
}
