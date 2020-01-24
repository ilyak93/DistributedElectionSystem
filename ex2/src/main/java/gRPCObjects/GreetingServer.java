package gRPCObjects;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import protos.GreeterGrpc;
import protos.Vote;
import protos.Vote.EndElectionsReply;
import protos.Vote.EndElectionsRequest;
import protos.Vote.StartElectionsReply;
import protos.Vote.StartElectionsRequest;
import protos.Vote.VotesCountForPartyReply;
import protos.Vote.VotesCountForPartyRequest;
import app.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.zookeeper.KeeperException;

import app.controllers.VotesMap;
import app.models.VotesCount;
import gRPCObjects.interceptors.ServerInterceptorImpl;
import gRPCObjects.paxos.FuturePaxosGreetingClient;

public class GreetingServer extends GreeterGrpc.GreeterImplBase {
    int id;
    private Server greetingServer;
    private String state;
    
    public GreetingServer(int id, int port, String state) {
        this.id = id;
        this.state = state;
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
    public void receiveVote(Vote.VoteRequest request, StreamObserver<Vote.VoteReply> responseObserver) {
        Vote.VoteReply rep = Vote.VoteReply
                .newBuilder()
                .setClientID(request.getClientID())
                .setParty(request.getParty())
                .setOriginState(request.getOriginState())
                .setCurrentState(request.getCurrentState())
                .setTimeStamp(request.getTimeStamp())
                .setServerID(id)
                .build();
        
        app.models.Vote remoteVote = new app.models.Vote(request.getClientID(),
    			request.getParty(),
    			request.getOriginState(),
    			request.getCurrentState(),
    			request.getTimeStamp());
        //the vote is received as part of the broadcast after the elections ended, so it needs to get into the votes map.
        if (app.Server.electionsEnded) {
	        boolean insertedToVotesMap = false;
	        while(!insertedToVotesMap) {
				synchronized(VotesMap.mutex) {
					try {
						app.models.Vote currentMapVote = VotesMap.get(request.getClientID());
						System.out.println(remoteVote.toString());
				        if((currentMapVote == null) || (request.getTimeStamp() >= currentMapVote.getTimeStamp())) {
							VotesMap.put(remoteVote.getClientId(), remoteVote);
				        }
						insertedToVotesMap = true;
					} catch(Exception e) {
						insertedToVotesMap = false;
					}
					VotesMap.mutex.notifyAll();
				}
			}
        }
        //if(!app.Server.electionsEnded) {
	        if((this.state.equals(rep.getOriginState())) && (!rep.getCurrentState().equals(rep.getOriginState()))) {
	        	try {
	        		Future<app.models.Vote> future = new FuturePaxosGreetingClient().calculate(app.Server.zkManager.getCurrentStateAddressesForPaxos(), app.Server.serverId, remoteVote);
	        		int voteNumber = app.Server.votesCounter.addAndGet(1);
	        		app.Server.votesInDistributionProcess.put(voteNumber, future);
	        		} catch(InterruptedException e) {
	        			System.out.println("Future interupted in receiveVote remoteVote: " + e.getMessage());
	        		} catch(KeeperException e) {
	        			System.out.println("Zookeeper exception in receiveVote remoteVote: " + e.getMessage());
	        		}
	        }
        //}
        
        responseObserver.onNext(rep);
        responseObserver.onCompleted();
    }
    
    @Override
    public void receiveStartElections(StartElectionsRequest request, StreamObserver<StartElectionsReply> responseObserver) {
    	app.Server.electionsStarted = true;
    	StartElectionsReply rep = StartElectionsReply.newBuilder().build();
    	responseObserver.onNext(rep);
    	responseObserver.onCompleted();
    }
    
    @Override
    public void receiveEndElections(EndElectionsRequest request, StreamObserver<EndElectionsReply> responseObserver) {
    	while(!app.Server.electionsEnded) {
    		synchronized(app.Server.sendingRemoteVoteMutex) {
    			if(app.Server.sendingRemoteVoteCounter == 0) {
        			app.Server.electionsStarted = true;
        			app.Server.electionsEnded = true;
        			app.Server.zkManager.registerFinishedRemoteSending();
        		}
    		}
    	}
    	EndElectionsReply rep = EndElectionsReply.newBuilder().build();
    	responseObserver.onNext(rep);
    	responseObserver.onCompleted();
    }
    
    @Override
    public void reciveVotesCount(VotesCountForPartyRequest request, StreamObserver<VotesCountForPartyReply> responseObserver) {
    	 Collection<VotesCount> votesCounts = VotesMap.countVotes().values();
    	 for (VotesCount votesCount : votesCounts) {
    		 VotesCountForPartyReply rep = VotesCountForPartyReply
    				 					.newBuilder()
    				 					.setState(this.state)
    				 					.setParty(votesCount.getParty())
    				 					.setVotesCount(votesCount.getCount())
    				 					.build();
    		 responseObserver.onNext(rep);
    	 }
    	 responseObserver.onCompleted();
    }
    
    @Override
    public void receiveStatus(VotesCountForPartyRequest request, StreamObserver<VotesCountForPartyReply> responseObserver) {
    	
    	Map<String, VotesCount> votesCounts = VotesMap.countVotes();
    	VotesCount votesCount = votesCounts.get(request.getParty());
    	if(votesCount == null) {
    		VotesCountForPartyReply rep = VotesCountForPartyReply.newBuilder()
    				.setParty(request.getParty())
    				.setState(request.getState())
    				.setVotesCount(0)
    				.build();
    		responseObserver.onNext(rep);
    	} else {
    		VotesCountForPartyReply rep = VotesCountForPartyReply.newBuilder()
    				.setParty(votesCount.getParty())
    				.setState(votesCount.getState())
    				.setVotesCount(votesCount.getCount())
    				.build();
    		responseObserver.onNext(rep);
    	}
    	responseObserver.onCompleted();
    }
}
