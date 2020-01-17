package app.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommandElections {
    
    private String command;
    
    public CommandElections(@JsonProperty(value = "command", required = true) String command) {
        this.command = command;
    }
    
    
    public CommandElections setCommand(String command) {
        this.command = command;
        return this;
    }
    
    public String getCommand() { return this.command; }
}