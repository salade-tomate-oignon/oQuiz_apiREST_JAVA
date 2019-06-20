package org.stoe.oquiz.websocket.client;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.stoe.oquiz.entity.FriendSocketMessage;

@ClientEndpoint
public class FriendSocketClient {
    private final String url = "ws://localhost:8080/oquiz/friend";
    private Session session;

    // **************************************************
    // Constructors
    // **************************************************
    public FriendSocketClient() {
        try {
            URI endpointURI = new URI(this.getUrl());
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            System.out.println("FriendSocketClient.constructor(): " + e.getMessage());
        }
    }

    // **************************************************
    // Getters and Setters
    // **************************************************
    public String getUrl() {
        return url;
    }

    // **************************************************
    // Public methods
    // **************************************************
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("FriendSocketClient: opened");
        this.session = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        this.session = null;
        System.out.println("FriendSocketClient: closed");
    }
    
    @OnMessage
    public void onMessage(String message) {
        System.out.println("client: received message " + message);
    }
    
    public void send(FriendSocketMessage message) {
        this.session.getAsyncRemote().sendText(message.encode());
    }
}
