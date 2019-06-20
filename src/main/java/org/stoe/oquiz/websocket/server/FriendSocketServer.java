package org.stoe.oquiz.websocket.server;

import java.io.IOException;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.stoe.oquiz.entity.FriendSocketMessage;


@ServerEndpoint("/friend")
public class FriendSocketServer {
    static HashMap<Integer, Session> sessionMap = new HashMap<Integer, Session>();

    // **************************************************
    // Constructors
    // **************************************************
	public FriendSocketServer() {
		// sessionMap = new HashMap<Integer, Session>();
	}
    
    // **************************************************
    // Public methods
    // **************************************************
	@OnOpen
    public void onOpen(Session session) {
		System.out.println("FriendSocketServer: opened");
    }
 
    @OnMessage
    public void onMessage(String msgStr, Session session) {
        FriendSocketMessage msgObj = FriendSocketMessage.decode(msgStr);
        
        if(!msgObj.getFromAPI().isEmpty()) {
            // Message provenant de l'API
            // TODO verify TOKEN
            if(true) {
                this.sendToUser(msgObj);
            }
        }
        else if(msgObj.getTopic().equals("authentication")) {
            // Message provenant d'un client
            // TODO verify TOKEN
            if(true) {
                FriendSocketServer.sessionMap.put(msgObj.getFrom(), session);
            }
        }
    }
 
    @OnClose
    public void onClose(Session session) {
    	System.out.println("FriendSocketServer: closed");
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("FriendSocketServer: error");
    }

    // **************************************************
    // Private methods
    // **************************************************
    private void sendToUser(FriendSocketMessage msg) {
        try {
            msg.setFromAPI("");
            if(FriendSocketServer.sessionMap.containsKey(msg.getTo())) {
                FriendSocketServer.sessionMap.get(msg.getTo()).getBasicRemote().sendText(msg.encode());
            }
		} catch (IOException e) {
			System.out.println("FriendSocketServer.sendToUser(): " + e.getMessage());
		}
    }
}
