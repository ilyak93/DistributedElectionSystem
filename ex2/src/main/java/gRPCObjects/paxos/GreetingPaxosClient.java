package gRPCObjects.paxos;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.google.common.util.concurrent.ListenableFuture;
import app.paxos.SessionsMap;
import gRPCObjects.interceptors.ClientInterceptorImpl;
import protos.Paxos.Session;
import protos.Paxos.Prepare;
import protos.Paxos.Promise;
import protos.Paxos.Accept;
import protos.Paxos.Accepted;
import protos.Paxos.Init;
import protos.Paxos.VotePax;
import protos.PaxosGreeterGrpc;
import app.models.Vote;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;

public class GreetingPaxosClient {
    
	private int serverId;
    private List<ManagedChannel> channels;
    private List<PaxosGreeterGrpc.PaxosGreeterFutureStub> futureStubs;
    
    public GreetingPaxosClient(List<String> addresses, int serverId) {
    	this.serverId = serverId;
    	this.channels = new ArrayList<>();
    	this.futureStubs = new ArrayList<>();
    	for (String address : addresses) {
    		String host = address.split(":")[0];
    		int port = Integer.parseInt(address.split(":")[1]);
    		ManagedChannel channel = ManagedChannelBuilder
                    .forAddress(host, port)
                    .intercept(new ClientInterceptorImpl(0))
                    .usePlaintext()
                    .build();
    		PaxosGreeterGrpc.PaxosGreeterFutureStub futureStub = PaxosGreeterGrpc.newFutureStub(channel);
    		channels.add(channel);
    		futureStubs.add(futureStub);
    	}
    }
    

    public void shutdown() {
    	for (ManagedChannel channel : channels) {
    		try {
    			channel.shutdown();
    		}catch(Exception e) {
    			
    		}
    	}
    }
    
    private OneTimeUseElement<ListenableFuture<Init>> sendInit(Init init, PaxosGreeterGrpc.PaxosGreeterFutureStub futureStub) {
    	OneTimeUseElement<ListenableFuture<Init>> initReply = new OneTimeUseElement<>(futureStub.uponReceivingInit(init));
    	return initReply;
    }
    
    private OneTimeUseElement<ListenableFuture<Promise>> sendPrepare(Prepare prepare, PaxosGreeterGrpc.PaxosGreeterFutureStub futureStub) {
    	OneTimeUseElement<ListenableFuture<Promise>> promise = new OneTimeUseElement<>(futureStub.uponReceivingPrepare(prepare));
    	return promise;
    }
    
    private OneTimeUseElement<ListenableFuture<Accepted>> sendAccept(Accept accept, PaxosGreeterGrpc.PaxosGreeterFutureStub futureStub) {
    	OneTimeUseElement<ListenableFuture<Accepted>> accepted = new OneTimeUseElement<>(futureStub.uponReceivingAccept(accept));
    	return accepted;
    }
    
    /*
     * this function will send the vote to all the other servers in its network, using the Paxos protocol.
     * the current process is the protocol leader.
     * all the other servers are all acceptors and also listeners.
     * the following function implements the proposer's role.
     * */
    public Vote sendVote(Vote vote) {
    	
    	int roundNumber = 0;
    	boolean isDecided = false;
    	Vote decidedVote = vote;
    	
    	Session session = SessionsMap.createNewSession(this.serverId, this.serverId, vote.getClientId());
    	int sessionID = session.getSessionID();
    	int leaderID = session.getLeaderID();
    	
    	Init init = Init
				.newBuilder()
				.setServerID(this.serverId)
				.setSessionID(sessionID)
				.setLeaderID(leaderID)
				.setVoterID(vote.getClientId())
				.build();
    	List<OneTimeUseElement<ListenableFuture<Init>>> initFutures = new ArrayList<>();
    	for (PaxosGreeterGrpc.PaxosGreeterFutureStub futureStub : futureStubs) {
    		initFutures.add(sendInit(init, futureStub));
		}
    	int recievedCounter = 0;
    	//Maybe check for all live servers with the zookeeper.
    	//wait until received from all servers
    	while(recievedCounter < futureStubs.size()) {
    		for (OneTimeUseElement<ListenableFuture<Init>> initFuture : initFutures) {
    			if ((!initFuture.isUsed()) && (initFuture.getElement().isDone())) {
    				try {
    					Init initReply = initFuture.getElement().get();
    					initFuture.use();
    					recievedCounter++;
    				} catch(ExecutionException |  InterruptedException e) {
    					System.out.println("caught an exception in GreetingPaxosClient::sendVote while trying to get value of init future: " + e.getMessage());
    				}
    			}
    		}
    	}
    	
    	// Finished to init all servers to start the protocols.
    	
    	// Start the protocols round until you get decision.
    	while(!isDecided) {
    		roundNumber++;
    		//Create a prepare message to send to all servers in the network.
    		Prepare prepare = Prepare
    							.newBuilder()
    							.setRoundNumber(roundNumber)
    							.setTimeStamp(vote.getTimeStamp())
    							.setServerID(this.serverId)
    							.setSessionID(sessionID)
    							.setLeaderID(leaderID)
    							.setVoterID(vote.getClientId())
    							.build();
    		
    		//Send the prepare message to all the servers in the network and wait until there is an answer from a quorum.
    		List<OneTimeUseElement<ListenableFuture<Promise>>> promiseFutures = new ArrayList<>();
    		List<Promise> promises = new ArrayList<>();
        	for (PaxosGreeterGrpc.PaxosGreeterFutureStub futureStub : futureStubs) {
        		promiseFutures.add(sendPrepare(prepare, futureStub));
    		}
        	recievedCounter = 0;
        	//Maybe check for all live servers with the zookeeper.
        	//wait until received from a quorum of promisses from the acceptors servers.
        	while(recievedCounter < (double)futureStubs.size()/2) {
        		for (OneTimeUseElement<ListenableFuture<Promise>> promiseFuture : promiseFutures) {
        			if ((!promiseFuture.isUsed()) && (promiseFuture.getElement().isDone())) {
        				try {
	        					Promise promise = promiseFuture.getElement().get();
	        					promiseFuture.use();
	        					recievedCounter++;
	        					promises.add(promise);
        				} catch(ExecutionException |  InterruptedException e) {
        					System.out.println("caught an exception in GreetingPaxosClient::sendVote while trying to get value of init future: " + e.getMessage());
        				} catch(Exception e) {
        					
        				}
        			}
        		}
        	}
        	for (OneTimeUseElement<ListenableFuture<Promise>> promiseFuture : promiseFutures) {
        		if(!promiseFuture.isUsed()) {
	        		if(!promiseFuture.getElement().isDone()) {
	        			try {
		        			Promise promise = promiseFuture.getElement().get();
		        			promises.add(promise);
	        			} catch(ExecutionException |  InterruptedException e) {
        					System.out.println("caught an exception in GreetingPaxosClient::sendVote while trying to get value of init future: " + e.getMessage());
        				} catch(Exception e) {
        					
        				}
	        		} else {
	        			promiseFuture.getElement().cancel(true);
	        		}
        		}
        	}
        	// Check that the client got acks from a quorum.
        	int acksInCurrentRoundCounter = 0;
        	for(Promise promise : promises) {
        		if (promise.getAck() && (promise.getRoundNumber() == roundNumber)) {
        			acksInCurrentRoundCounter++;
        		}
        	}
        	
        	// Got acks from a qurum of acceptors.
        	if(acksInCurrentRoundCounter > (double)futureStubs.size()/2) {
        		        		
        		//in our implementation the client is the one with the highest round, and it is always the leader.
        		//we would like to get the latest vote
        		// so instead of choosing the promise with the highest last good round, we will choose the promise with the
        		// highest vote timestamp.

        		// build VotePax from the client vote and compare it to the other servers.
        		VotePax VoteWithMaxTimeStamp = VotePax
        				.newBuilder()
        				.setClientID(vote.getClientId())
        				.setParty(vote.getParty())
        				.setOriginState(vote.getOriginState())
        				.setCurrentState(vote.getCurrentState())
        				.setTimeStamp(vote.getTimeStamp())
        				.setSessionID(sessionID)
        				.setLeaderID(leaderID)
        				.build();

        		for (Promise promise : promises) {
        			if(promise.getVote().getTimeStamp() > VoteWithMaxTimeStamp.getTimeStamp()) {
        				VoteWithMaxTimeStamp = promise.getVote();
        			}
        		}
        		
        		//Send an Accept message to all acceptors.
        		Accept accept = Accept
        				.newBuilder()
        				.setRoundNumber(roundNumber)
        				.setVote(VoteWithMaxTimeStamp)
        				.setServerID(this.serverId)
        				.setSessionID(sessionID)
        				.setLeaderID(leaderID)
        				.build();
        		
        		//Send the accept message to all the servers in the network and wait until there is an answer from a quorum.
        		List<OneTimeUseElement<ListenableFuture<Accepted>>> acceptedFutures = new ArrayList<>();
        		List<Accepted> accepteds = new ArrayList<>();
            	for (PaxosGreeterGrpc.PaxosGreeterFutureStub futureStub : futureStubs) {
            		acceptedFutures.add(sendAccept(accept, futureStub));
        		}
            	recievedCounter = 0;
            	//Maybe check for all live servers with the zookeeper.
            	//wait until received from a quorum of accepted from the acceptors servers.
            	while(recievedCounter < (double)futureStubs.size()/2) {
            		for (OneTimeUseElement<ListenableFuture<Accepted>> acceptedFuture : acceptedFutures) {
            			if ((!acceptedFuture.isUsed()) && (acceptedFuture.getElement().isDone())) {
            				try {
            					Accepted accepted = acceptedFuture.getElement().get();
            					acceptedFuture.use();
            					recievedCounter++;
            					accepteds.add(accepted);
            				} catch(ExecutionException |  InterruptedException e) {
            					System.out.println("caught an exception in GreetingPaxosClient::sendVote while trying to get value of init future: " + e.getMessage());
            				} catch(Exception e) {
            					
            				}
            			}
            		}
            	}
            	
            	// Check if got accepted with current round from a quorum and ack
            	acksInCurrentRoundCounter = 0;
            	for(Accepted accepted : accepteds) {
            		if((accepted.getAck()) && (accepted.getRoundNumber() == roundNumber)) {
            			acksInCurrentRoundCounter++;
            		}
            	}
            	if(acksInCurrentRoundCounter > (double)futureStubs.size()/2) {
            		decidedVote = new Vote(VoteWithMaxTimeStamp.getClientID(),
            									VoteWithMaxTimeStamp.getParty(),
            									VoteWithMaxTimeStamp.getOriginState(),
            									VoteWithMaxTimeStamp.getCurrentState(),
            									VoteWithMaxTimeStamp.getTimeStamp()); 
            		isDecided = true;
            	}
        	}
    	}
    	return decidedVote;
    }   
}
