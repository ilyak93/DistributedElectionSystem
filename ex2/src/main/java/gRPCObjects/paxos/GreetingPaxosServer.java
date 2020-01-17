package gRPCObjects.paxos;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import gRPCObjects.interceptors.ServerInterceptorImpl;
import protos.Paxos.Prepare;
import protos.Paxos.Promise;
import protos.Paxos.Session;
import protos.Paxos.Accept;
import protos.Paxos.Accepted;
import protos.Paxos.Init;
import protos.PaxosGreeterGrpc;
import app.paxos.PrepareAndPromise;
import app.paxos.SessionKey;
import app.controllers.VotesMap;
import app.models.Vote;
import app.paxos.AcceptAndAccepted;
import app.paxos.InitAndSession;
import app.paxos.SessionsMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GreetingPaxosServer extends PaxosGreeterGrpc.PaxosGreeterImplBase {
    int id;
    private Server greetingServer;
    static private AtomicInteger sessionsCounter = new AtomicInteger(0);
    
    public GreetingPaxosServer(int id, int port) {
        this.id = id;
        try {
            greetingServer = ServerBuilder.forPort(port)
                    .addService(this)
                    .intercept(new ServerInterceptorImpl())
                    .build()
                    .start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    void shutdown() {
        greetingServer.shutdown();
    }
    
    @Override
    public void uponReceivingInit(Init init, StreamObserver<Init> responseObserver) {
    	Session newSession = SessionsMap.createNewSession(init.getLeaderID(), this.id, init.getVoterID(), init.getSessionID());
    	InitAndSession initAndSession = new InitAndSession(init, newSession);
    	Init initReply = initAndSession.getInit();
    	responseObserver.onNext(initReply);
        responseObserver.onCompleted();
    }
    
    @Override
    public void uponReceivingPrepare(Prepare prepare, StreamObserver<Promise> responseObserver) {
    	SessionKey sessionKey = new SessionKey(prepare.getSessionID(), prepare.getLeaderID());
    	Session session = SessionsMap.get(sessionKey);
    	PrepareAndPromise prepareAndPromise = new PrepareAndPromise(prepare, session);
    	SessionsMap.put(sessionKey, prepareAndPromise.getSession());
    	Promise promise = prepareAndPromise.getPromise();
    	responseObserver.onNext(promise);
        responseObserver.onCompleted();
    }
    
    @Override
    public void uponReceivingAccept(Accept accept, StreamObserver<Accepted> responseObserver) { 
    	SessionKey sessionKey = new SessionKey(accept.getSessionID(), accept.getLeaderID());
    	Session session = SessionsMap.get(sessionKey);
    	AcceptAndAccepted acceptAndAccepted = new AcceptAndAccepted(accept, session);
    	SessionsMap.remove(sessionKey);
    	Accepted accepted = acceptAndAccepted.getAccepted();
		Vote acceptedVote = new Vote(accepted.getVote().getClientID(),
				 accepted.getVote().getParty(),
				 accepted.getVote().getOriginState(),
				 accepted.getVote().getCurrentState(),
				 accepted.getVote().getTimeStamp());
		if(accepted.getAck()) {
			boolean insertedToVotesMap = false;
			while(!insertedToVotesMap) {
				synchronized(VotesMap.mutex) {
					try {
						Vote currentVoteInMap = VotesMap.get(acceptedVote.getClientId());
						if((currentVoteInMap == null) || (acceptedVote.getTimeStamp() >= currentVoteInMap.getTimeStamp())) {
							VotesMap.put(acceptedVote.getClientId(), acceptedVote);
						}
						insertedToVotesMap = true;
					} catch(Exception e) {
						insertedToVotesMap = false;
					}
					VotesMap.mutex.notifyAll();
				}
			}
		}
    	responseObserver.onNext(accepted);
        responseObserver.onCompleted();
    }
}
