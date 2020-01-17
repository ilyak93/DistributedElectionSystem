package app.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import app.Server;

@Data
public class VotesCount {
    
    private String party;
    private int count;
    private String state;
    
    public VotesCount(@JsonProperty(value = "party", required = true) String party) {
        this.party = party;
        this.count = 1;
        this.state = Server.state;
    }
    
    public VotesCount(@JsonProperty(value = "party", required = true) String party,
    		@JsonProperty(value = "count", required = true) int count) {
        this.party = party;
        this.count = count;
        this.state = Server.state;
    }
    
    public VotesCount(@JsonProperty(value = "party", required = true) String party,
    		@JsonProperty(value = "count", required = true) int count,
    		@JsonProperty(value = "state", required = true) String state) {
        this.party = party;
        this.count = count;
        this.state = state;
    }
    
    public VotesCount add(int moreCount) {
    	this.count += moreCount;
    	return this;
    }
    
    public VotesCount add() {
    	this.count++;
    	return this;
    }
    
    public String getParty() {
    	return this.party;
    }
    
    public int getCount() {
    	return this.count;
    }
    
    public String getState() {
    	return this.state;
    }
}
	
