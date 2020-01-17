package app.models;


import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import app.Server;

@Data
public class Distribution {
    
    private String party;
    private int electors;
    
    public Distribution(@JsonProperty(value = "party", required = true) String party,
    		@JsonProperty(value = "electors", required = true) int electors) {
        this.party = party;
        this.electors = electors;
    }
    
    public String getParty() {
    	return this.party;
    }
    
    public int getElectors() {
    	return this.electors;
    }
}