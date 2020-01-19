package app.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import app.Server;

@Data
public class Vote {
    private int clientId;
    private String party;
    private String originState;
    private String currentState;
    private long timeStamp;
    private boolean counted;
    private boolean broadcastedInState;
    private boolean valid;
    private int serverId;
    
    public Vote(@JsonProperty(value = "clientId", required = true) int clientId,
			@JsonProperty(value = "party", required = true) String party) {
		this.clientId = clientId;
		this.party = party;
		this.originState = Server.clientIdToOriginState.get(clientId);
		this.currentState = Server.state;
		//this.timeStamp = System.currentTimeMillis();
		this.timeStamp = Server.zkManager.getTime();
		this.counted = false;
		this.broadcastedInState = false;
		this.valid = this.originState != null;
		this.serverId = Server.serverId;
    }
    
    public Vote(int clientId, String party, String originState, String currentState) {
	    this.clientId = clientId;
	    this.party = party;
	    this.originState = originState;
	    this.currentState = currentState;
	    this.timeStamp = System.currentTimeMillis();
	    this.counted = false;
	    this.broadcastedInState = false;
	    this.valid = this.originState != null;
	    this.serverId = Server.serverId;
    }
    
    public Vote(int clientId, String party, String originState, String currentState, long timeStamp) {
	    this.clientId = clientId;
	    this.party = party;
	    this.originState = originState;
	    this.currentState = currentState;
	    this.timeStamp = timeStamp;
	    this.counted = false;
	    this.broadcastedInState = false;
	    this.valid = this.originState != null;
	    this.serverId = Server.serverId;
    }
    
    
    public Vote setClientId(int clientId) {
        this.clientId = clientId;
        return this;
    }
    public Vote setParty(String party) {
        this.party = party;
        return this;
    }
    public Vote setOriginState(String originState) {
        this.originState = originState;
        return this;
    }
    public Vote setCurrentState(String currentState) {
        this.currentState = currentState;
        return this;
    }
    
    public Vote setTimeStamp(long timeStamp) {
    	this.timeStamp = timeStamp;
    	return this;
    }
    
    public int getClientId() { return clientId; }
    public String getParty() { return party; }
    public String getOriginState() { return originState; }
    public String getCurrentState() { return currentState; }
    public long getTimeStamp() { return timeStamp; }
    
    public boolean isCounted() {
    	return this.counted;
    }
    
    public void count() {
    	this.counted = true;
    }
    
    public boolean isBroadcastedInState() {
    	return this.broadcastedInState;
    }
    
    public void broadcastInState() {
    	this.broadcastedInState = true;
    }
    
    public String toString() {
    	return clientId + "," + party + "," + originState + "," + currentState + "," + timeStamp;
    }
    
    public boolean isValid() {
    	return this.valid;
    }
    
    public int getServerId() {
    	return this.serverId;
    }
}