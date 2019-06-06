package org.stoe.oquiz.service.functionality;

import javax.ws.rs.core.Response;

public interface FriendInterface {
	public Response friendRequest(int authorId, String pseudoFriend);
}
