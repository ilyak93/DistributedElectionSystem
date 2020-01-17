package app.paxos;

import java.util.Objects;

public class SessionKey {
	private int sessionId;
	private int leaderId;
	
	public SessionKey(int sessionId, int leaderId){
		this.sessionId = sessionId;
		this.leaderId = leaderId;
	}
	
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject instanceof SessionKey) {
			SessionKey otherSessionKey = (SessionKey) otherObject;
	        return otherSessionKey == null ? false : ((otherSessionKey.sessionId == this.sessionId) && (otherSessionKey.leaderId == this.leaderId));
	    }
	    return false;
	}
	
	public String toString() {
		return Integer.toString(this.sessionId) + "," + Integer.toString(this.leaderId);
	}
	
	public int hashCode() {
		return Objects.hash(this.sessionId, this.leaderId);
	}
}
