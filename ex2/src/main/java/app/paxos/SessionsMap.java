package app.paxos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import protos.Paxos.Session;
import protos.Paxos.VotePax;;

public class SessionsMap {

	private static volatile ConcurrentMap<SessionKey, Session> sessionsMap = new ConcurrentHashMap<>();
	private static volatile ConcurrentMap<SessionKey, Boolean> flagMap = new ConcurrentHashMap<>();
	private static volatile AtomicInteger sessionsCounter = new AtomicInteger(0);
	public static volatile Integer sessionCounterMutex = new Integer(-1);
	
	
	
	public static Session createNewSession(int leaderId, int serverId, int voterID){
		int newSessionId = 0;
		boolean gotNewId = false;
		while(!gotNewId) {
			try {
				synchronized(sessionCounterMutex) {
					newSessionId = sessionsCounter.addAndGet(1);
					gotNewId = true;
				}
			} catch (Exception e) {
				
			}
		}
		Session newSession = Session
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
		if(flagMap.get(sessionKey) == null) {
			System.out.println("inserted first time sessionId = " + newSessionId + " leaderId = " + leaderId);
			flagMap.put(sessionKey, false);
		} else {
			System.out.println("inserted second time sessionId = " + newSessionId + " leaderId = " + leaderId);
		}
		if(flagMap.get(sessionKey)) {
			System.out.println("inserted after removed time sessionId = " + newSessionId + " leaderId = " + leaderId);
		}
		sessionsMap.put(sessionKey, newSession);
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
		flagMap.put(sessionKey, true);
	}
	
	public static Map<SessionKey, Session> getMap() {
		return sessionsMap;
	}
}
