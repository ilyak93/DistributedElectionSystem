package app.readFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

//get a file of states. each state is in a new line.
public class ReadVoters {
	
	//Map from clientIdToOriginState
	private Map<Integer, String> votersMap = new HashMap<>();
	
	public ReadVoters(String votersFilePath){
		try {
			List<String> allVotersFromFile = Files.readAllLines(Paths.get(votersFilePath));
			for (String voterFromFile : allVotersFromFile) {
				Integer voterId =  Integer.parseInt(voterFromFile.split(",")[0]);
				String origineState = voterFromFile.split(",")[1];
				votersMap.put(voterId, origineState);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Map<Integer, String> getVotersMap(){
		return this.votersMap;
	}
}