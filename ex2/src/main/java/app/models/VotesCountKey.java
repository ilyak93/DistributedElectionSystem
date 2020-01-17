package app.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import app.Server;

@Data
public class VotesCountKey {
	
	 String party;
     String state;
     
	public VotesCountKey(@JsonProperty(value = "party", required = true) String party,
			@JsonProperty(value = "state", required = true) String state) {
		this.party = party;
		this.state = state;
	}
	
    public int hashCode() {
		return Objects.hash(this.state, this.party);
	}
    
    public VotesCountKey setParty(String party) {
    	this.party = party;
    	return this;
    }
    
    public VotesCountKey setState(String state) {
    	this.state = state;
    	return this;
    }
    
    public String getParty() {
    	return this.party;
    }
    public String getState() {
    	return this.state;
    }
    
    
}