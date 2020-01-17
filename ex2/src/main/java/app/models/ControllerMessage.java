package app.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ControllerMessage {
    
    private String messsage;
    
    public ControllerMessage() {
        this.messsage = "";
    }
    
    public ControllerMessage(@JsonProperty(value = "messsage", required = true) String messsage) {
        this.messsage = messsage;
    }
    
    
    public ControllerMessage setMessage(String messsage) {
        this.messsage = messsage;
        return this;
    }
    
    public String getMessage() { return this.messsage; }
}