package app.paxos;

import java.util.Map;

import app.controllers.VotesMap;
import app.models.Vote;
import protos.Paxos.Prepare;
import protos.Paxos.Promise;
import protos.Paxos.Session;
import protos.Paxos.VotePax;

public class PrepareAndPromise extends PaxosAction {

	private Promise promise;
		
	public PrepareAndPromise(Prepare prepare, Session session){
		
		Vote currentMapVote = VotesMap.get(prepare.getVoterID());
		
		if (prepare.getRoundNumber() > session.getLastRound()) {
			
			if(currentMapVote == null) {
				this.promise = Promise
						.newBuilder()
						.setRoundNumber(prepare.getRoundNumber())
						.setAck(true)
						.setLastGoodRound(session.getLastGoodRound())
						.setServerID(session.getServerID())
						.setSessionID(session.getSessionID())
						.setLeaderID(session.getLeaderID())
						.build();
				
				this.session = Session
						.newBuilder()
						.setSessionID(session.getSessionID())
						.setServerID(session.getServerID())
						.setLastRound(session.getLastRound())
						.setLastGoodRound(session.getLastGoodRound())
						.setIsDecided(session.getIsDecided())
						.setLeaderID(session.getLeaderID())
						.build();
				
			}else {
				VotePax currentMapVotePax = VotePax
						.newBuilder()
						.setClientID(currentMapVote.getClientId())
						.setParty(currentMapVote.getParty())
						.setOriginState(currentMapVote.getOriginState())
						.setCurrentState(currentMapVote.getCurrentState())
						.setTimeStamp(currentMapVote.getTimeStamp())
						.setSessionID(session.getSessionID())
						.setLeaderID(session.getLeaderID())
						.build();
				
				this.promise = Promise
						.newBuilder()
						.setRoundNumber(prepare.getRoundNumber())
						.setAck(true)
						.setLastGoodRound(session.getLastGoodRound())
						.setVote(currentMapVotePax)
						.setServerID(session.getServerID())
						.setSessionID(session.getSessionID())
						.setLeaderID(session.getLeaderID())
						.build();
				
				this.session = Session
						.newBuilder()
						.setSessionID(session.getSessionID())
						.setServerID(session.getServerID())
						.setLastRound(session.getLastRound())
						.setLastGoodRound(session.getLastGoodRound())
						.setVote(currentMapVotePax)
						.setIsDecided(session.getIsDecided())
						.setLeaderID(session.getLeaderID())
						.build();
			}
			
		} else {
			this.promise = Promise
					.newBuilder()
					.setRoundNumber(prepare.getRoundNumber())
					.setAck(false)
					.setLastGoodRound(session.getLastGoodRound())
					.setVote(session.getVote())
					.setServerID(session.getServerID())
					.setSessionID(session.getSessionID())
					.setLeaderID(session.getLeaderID())
					.build();
			if(currentMapVote == null) {
				this.session = Session
						.newBuilder()
						.setSessionID(session.getSessionID())
						.setServerID(session.getServerID())
						.setLastRound(session.getLastRound())
						.setLastGoodRound(session.getLastGoodRound())
						.setIsDecided(session.getIsDecided())
						.setLeaderID(session.getLeaderID())
						.build();
			} else {
				
				VotePax currentMapVotePax = VotePax
						.newBuilder()
						.setClientID(currentMapVote.getClientId())
						.setParty(currentMapVote.getParty())
						.setOriginState(currentMapVote.getOriginState())
						.setCurrentState(currentMapVote.getCurrentState())
						.setTimeStamp(currentMapVote.getTimeStamp())
						.setSessionID(session.getSessionID())
						.setLeaderID(session.getLeaderID())
						.build();
				
				
				this.session = Session
						.newBuilder()
						.setSessionID(session.getSessionID())
						.setServerID(session.getServerID())
						.setLastRound(session.getLastRound())
						.setLastGoodRound(session.getLastGoodRound())
						.setVote(currentMapVotePax)
						.setIsDecided(session.getIsDecided())
						.setLeaderID(session.getLeaderID())
						.build();
			}
		}
	}
	
	public Promise getPromise() {
		return this.promise;
	}
}
