package org.stoe.oquiz.service.functionality;

import javax.ws.rs.core.Response;

public interface FriendInterface {
	public Response friendRequest(int authorId, String pseudoFriend);
    
    public Response getAllfriendRequests(int userId);
    
    public Response getAllBlockedfriends(int userId);
    
    public Response getAllfriends(int userId);
}
