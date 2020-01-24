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
import protos.Paxos.Commit;
import protos.Paxos.Finish;
import protos.Paxos.Init;
import protos.PaxosGreeterGrpc;
import app.paxos.PrepareAndPromise;
import app.paxos.SessionKey;
import app.controllers.VotesMap;
import app.models.Vote;
import app.paxos.AcceptAndAccepted;
import app.paxos.InitAndSession;
import app.paxos.SessionsMap;

import java.util.Map;
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
    	SessionKey newSessionKey = new SessionKey(newSession.getSessionID(), newSession.getLeaderID());
    	SessionsMap.put(newSessionKey, newSession);
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
    	SessionsMap.put(sessionKey, acceptAndAccepted.getSession());
    	Accepted accepted = acceptAndAccepted.getAccepted();
    	responseObserver.onNext(accepted);
        responseObserver.onCompleted();
    }
    
    @Override
    public void uponReceivingCommit(Commit commit, StreamObserver<Finish> responseObserver) {
    	Vote commitedVote = new Vote(commit.getVote().getClientID(),
    			commit.getVote().getParty(),
    			commit.getVote().getOriginState(),
    			commit.getVote().getCurrentState(),
    			commit.getVote().getTimeStamp());
    	boolean insertedToVotesMap = false;
		while(!insertedToVotesMap) {
			synchronized(VotesMap.mutex) {
				try {
					Vote currentVoteInMap = VotesMap.get(commitedVote.getClientId());
					System.out.println(commitedVote.toString());
					if((currentVoteInMap == null) || (commitedVote.getTimeStamp() >= currentVoteInMap.getTimeStamp())) {
						VotesMap.put(commitedVote.getClientId(), commitedVote);
					}
					insertedToVotesMap = true;
				} catch(Exception e) {
					insertedToVotesMap = false;
				}
				VotesMap.mutex.notifyAll();
			}
		}
		SessionKey sessionKey = new SessionKey(commit.getSessionID(), commit.getLeaderID());
    	SessionsMap.remove(sessionKey);
    	Finish finish = Finish.newBuilder().build();
    	responseObserver.onNext(finish);
        responseObserver.onCompleted();
    }
    
}
