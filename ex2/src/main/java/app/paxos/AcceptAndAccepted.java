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
		
		System.out.println("inside AcceptAndAccepted");
		
		System.out.println("accept vote is: " + accept.getVote().toString());
		System.out.println("session vote is: " + session.getVote().toString());
		
		
		Vote currentMapVote = VotesMap.get(accept.getVote().getClientID());
		if(currentMapVote == null) {
			System.out.println("currentMapVote == null");
			System.out.println("accept.getRoundNumber() = " + accept.getRoundNumber());
			System.out.println("session.getLastRound() = " + session.getLastRound());
			if((accept.getRoundNumber() == session.getLastRound()) || (session.getLastRound() == 0)) {
				
				System.out.println("Ack is true");
				
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
				
					System.out.println("Ack is false");
				
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
			System.out.println("currentMapVote != null");
			System.out.println("accept.getVote().getTimeStamp() = " + accept.getVote().getTimeStamp());
			System.out.println("currentMapVote.getTimeStamp() = " + currentMapVote.getTimeStamp());
			System.out.println("currentMapVote.getTimeStamp() = " + currentMapVote.getTimeStamp());
			System.out.println("accept.getRoundNumber() = " + accept.getRoundNumber());
			System.out.println("session.getLastRound() = " + session.getLastRound());
			if((accept.getVote().getTimeStamp() >= currentMapVote.getTimeStamp()) && 
					(accept.getRoundNumber() == session.getLastRound()) || (session.getLastRound() == 0)) {

					System.out.println("Ack is true");
					
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
				
				System.out.println("Ack is false");
				
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
