package app.paxos;

import protos.Paxos.Init;
import protos.Paxos.Session;

public class InitAndSession extends PaxosAction {
	
	private Init init;
	
	public InitAndSession(Init init, Session session){
		this.session = session;
		this.init = Init
				.newBuilder()
				.setServerID(init.getServerID())
				.setSessionID(init.getSessionID())
				.setLeaderID(session.getLeaderID())
				.build();
	}
	
	public Init getInit() {
		return this.init;
	}
}
