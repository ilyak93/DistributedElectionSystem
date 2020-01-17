package app.paxos;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import protos.Paxos.Session;
import protos.Paxos.VotePax;;

public class SessionsMap {

	private static ConcurrentMap<SessionKey, Session> sessionsMap = new ConcurrentHashMap<>();
	private static AtomicInteger sessionsCounter = new AtomicInteger(0);
	
	public static Session createNewSession(int leaderId, int serverId, int voterID){
		int newSessionId = sessionsCounter.addAndGet(1);
		Session newSessioin = Session
								.newBuilder()
								.setSessionID(newSessionId)
								.setServerID(serverId)
								.setLastRound(0)
								.setLastGoodRound(0)
								.setIsDecided(false)
								.setLeaderID(leaderId)
								.setVoterID(voterID)
								.build();
		SessionKey sessionKey = new SessionKey(newSessionId, leaderId);
		sessionsMap.put(sessionKey, newSessioin);
		return sessionsMap.get(sessionKey);
	}
	
	public static Session createNewSession(int leaderId, int serverId, int voterID, int sessionId){

		Session newSessioin = Session
								.newBuilder()
								.setSessionID(sessionId)
								.setServerID(serverId)
								.setLastRound(0)
								.setLastGoodRound(0)
								.setIsDecided(false)
								.setLeaderID(leaderId)
								.setVoterID(voterID)
								.build();
		SessionKey sessionKey = new SessionKey(sessionId, leaderId);
		sessionsMap.put(sessionKey, newSessioin);
		return sessionsMap.get(sessionKey);
	}
	
	
	public static Session get(SessionKey sessionKey) {
		return sessionsMap.get(sessionKey);
	}
	
	public static void put (SessionKey sessionKey, Session session) {
		sessionsMap.put(sessionKey, session);
	}
	
	public static void remove(SessionKey sessionKey) {
		sessionsMap.remove(sessionKey);
	}
}
