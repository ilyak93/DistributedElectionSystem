package app.paxos;

import app.controllers.VotesMap;
import app.models.Vote;
import protos.Paxos.Accept;
import protos.Paxos.Accepted;
import protos.Paxos.Session;
import protos.Paxos.VotePax;

public class AcceptAndAccepted extends PaxosAction{
	
	private Accepted accepted;
	
	public AcceptAndAccepted(Accept accept, Session session){
		
		Vote currentMapVote = VotesMap.get(accept.getVote().getClientID());
		if(currentMapVote == null) {
			if((accept.getRoundNumber() == session.getLastRound()) || (session.getLastRound() == 0)) {
				this.session = Session
						.newBuilder()
						.setSessionID(session.getSessionID())
						.setServerID(session.getServerID())
						.setLastRound(accept.getRoundNumber())
						.setLastGoodRound(accept.getRoundNumber())
						.setVote(accept.getVote())
						.setIsDecided(true)
						.setLeaderID(session.getLeaderID())
						.build();
				this.accepted = Accepted
						.newBuilder()
						.setRoundNumber(accept.getRoundNumber())
						.setAck(true)
						.setVote(accept.getVote())
						.setServerID(session.getServerID())
						.setSessionID(session.getSessionID())
						.setLeaderID(session.getLeaderID())
						.build();
			}else {
					this.session = Session
							.newBuilder()
							.setSessionID(session.getSessionID())
							.setServerID(session.getServerID())
							.setLastRound(session.getLastRound())
							.setLastGoodRound(session.getLastGoodRound())
							.setIsDecided(session.getIsDecided())
							.setLeaderID(session.getLeaderID())
							.build();
					this.accepted = Accepted
							.newBuilder()
							.setRoundNumber(accept.getRoundNumber())
							.setAck(false)
							.setServerID(session.getServerID())
							.setSessionID(session.getSessionID())
							.setLeaderID(session.getLeaderID())
							.build();
			}
		} else {
			
			if((accept.getVote().getTimeStamp() >= currentMapVote.getTimeStamp()) && 
					(accept.getRoundNumber() == session.getLastRound()) || (session.getLastRound() == 0)) {

					
					
					this.session = Session
							.newBuilder()
							.setSessionID(session.getSessionID())
							.setServerID(session.getServerID())
							.setLastRound(accept.getRoundNumber())
							.setLastGoodRound(accept.getRoundNumber())
							.setVote(accept.getVote())
							.setIsDecided(true)
							.setLeaderID(session.getLeaderID())
							.build();
					this.accepted = Accepted
							.newBuilder()
							.setRoundNumber(accept.getRoundNumber())
							.setAck(true)
							.setVote(accept.getVote())
							.setServerID(session.getServerID())
							.setSessionID(session.getSessionID())
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
				this.accepted = Accepted
						.newBuilder()
						.setRoundNumber(accept.getRoundNumber())
						.setAck(false)
						.setVote(currentMapVotePax)
						.setServerID(session.getServerID())
						.setSessionID(session.getSessionID())
						.setLeaderID(session.getLeaderID())
						.build();
			}
		}
	}
	
	public Accepted getAccepted() {
		return this.accepted;
	}
}
