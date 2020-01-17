package app.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import app.models.Distribution;
import app.models.Vote;
import app.models.VotesCount;
import app.models.VotesCountKey;

	
public class VotesMap {
	
	private static ConcurrentMap<Integer, Vote> votesMap = new ConcurrentHashMap<>();
	public static Integer mutex = new Integer(-1);
	private static Map<String, VotesCount> countedVotes;
	
	public static Vote get(int clientId) {
		return votesMap.get(clientId);
	}
	
	public static void put (int clientId, Vote vote) {
		votesMap.put(clientId, vote);
	}
	
	public static Collection<Vote> getAll(){
		return votesMap.values();
	}
	
	public static List<Vote> copyAll() {
		
		while(true) {
			synchronized(VotesMap.mutex) {
				List<Vote> broadcastVotes = new ArrayList<>(votesMap.values());
				mutex.notifyAll();
				return broadcastVotes;
			}
		}
	}
	
	public static int size() {
		return votesMap.size();
	}
	
	public static Map<String, VotesCount> countVotes(){
		boolean copied = false;
		List<Vote> votes = new ArrayList<>();
		Map<String, VotesCount> votesCounts = new HashMap<>();
		while(!copied) {
			synchronized(VotesMap.mutex) {
				votes = new ArrayList<>(votesMap.values());
				mutex.notifyAll();
				copied = true;
			}
		}
		for (Vote vote : votes) {
			VotesCount votesCount = votesCounts.get(vote.getParty());
			if(votesCount == null) {
				votesCount = new VotesCount(vote.getParty());
				votesCounts.put(vote.getParty(), votesCount);
			}else{
				votesCount.add();
			}
		}
		countedVotes = votesCounts;
		return votesCounts;
	}
	
	public static List<Distribution>  getDistribution(Map<VotesCountKey ,VotesCount> allVotesCounts, Map<String, Integer> stateToElectors){
		Map<String, Map<String, Integer>> stateKeyToPartyAndVotesMap = new HashMap<>();
		for(VotesCount votesCount : allVotesCounts.values()) {
			Map<String, Integer> partyAndVotesMap = stateKeyToPartyAndVotesMap.get(votesCount.getState());
			if(partyAndVotesMap == null) {
				partyAndVotesMap = new HashMap<>();
				stateKeyToPartyAndVotesMap.put(votesCount.getState(), partyAndVotesMap);
			}
			partyAndVotesMap.put(votesCount.getParty(), votesCount.getCount());
		}
		Map<String, String> stateToWinningParty = new HashMap<>();
		Map<String, VotesCount> votesCountMap = new HashMap<>();
		for(Map.Entry<String, Map<String, Integer>> stateTopartyAndVotesMap  : stateKeyToPartyAndVotesMap.entrySet()) {
			String winningParty = "";
			int winningVotes = 0;
			String state = stateTopartyAndVotesMap.getKey();
			for (Map.Entry<String, Integer> partyAndVotes : stateTopartyAndVotesMap.getValue().entrySet()) {
				if(partyAndVotes.getValue() > winningVotes) {
					winningParty = partyAndVotes.getKey();
					winningVotes = partyAndVotes.getValue();
				}
			}
			VotesCount votesCount = new VotesCount(winningParty, stateToElectors.get(state) , state);
			votesCountMap.put(state, votesCount);
		}
		//count distribution on all USA.
		Map<String, Integer> partyToElectors = new HashMap<>();
		for(VotesCount voteCount : votesCountMap.values()) {
			Integer electorsCounter = partyToElectors.get(voteCount.getParty());
			if(electorsCounter == null) {
				partyToElectors.put(voteCount.getParty(), voteCount.getCount());
			} else {
				partyToElectors.put(voteCount.getParty(), electorsCounter + voteCount.getCount());
			}
		}
		List<Distribution> distributions = new ArrayList<>();
		for(Map.Entry<String, Integer> partyToElectorsEntry : partyToElectors.entrySet()) {
			distributions.add(new Distribution(partyToElectorsEntry.getKey(), partyToElectorsEntry.getValue()));
		}
		return distributions;
		
		
	}
	
}
