package app.readFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

//get a file of states. each state is in a new line.
public class ReadStates {
	
	// Map from state to electors
	private  Map<String, Integer> statesMap = new HashMap<>();
	
	public ReadStates(String statesFilePath){
		try {
			List<String> allStatesFromFile = Files.readAllLines(Paths.get(statesFilePath));
			for (String stateFromFile : allStatesFromFile) {
				String state = stateFromFile.split(",")[0];
				Integer electors =  Integer.parseInt(stateFromFile.split(",")[1]);
				statesMap.put(state, electors);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, Integer> getStatesMap(){
		return this.statesMap;
	}
}