package gRPCObjects;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import protos.GreeterGrpc;
import protos.Vote.VoteRequest;
import protos.Vote.VotesCountForPartyReply;
import protos.Vote.VotesCountForPartyRequest;
import protos.Vote.VoteReply;
import protos.Vote.StartElectionsReply;
import protos.Vote.StartElectionsRequest;
import protos.Vote.EndElectionsRequest;
import protos.Vote.EndElectionsReply;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import com.google.common.util.concurrent.ListenableFuture;
import gRPCObjects.interceptors.ClientInterceptorImpl;
import gRPCObjects.paxos.OneTimeUseElement;
import app.models.VotesCount;
import app.models.VotesCountKey;;



public class GreetingClient {
    
    
	private GreeterGrpc.GreeterBlockingStub  singleBlockingStub;
	private ManagedChannel singleBlockingChannel;
	
	private List<GreeterGrpc.GreeterBlockingStub>  blockingStubs;
	private List<ManagedChannel> blockingChannels;
	
	private GreeterGrpc.GreeterFutureStub singleFutureStub;
    private ManagedChannel singleFutureChannel;
    
    private List<GreeterGrpc.GreeterFutureStub> futureStubs;
    private List<ManagedChannel> futureChannels;
    private List<ListenableFuture<VoteReply>> sendsInProcess;

    public GreetingClient(String address) {
    	String host = address.split(":")[0];
		int port = Integer.parseInt(address.split(":")[1]);
    	singleFutureChannel = ManagedChannelBuilder
                .forAddress(host, port)
                .intercept(new ClientInterceptorImpl(0))
                .usePlaintext()
                .build();
    	singleFutureStub = GreeterGrpc.newFutureStub(singleFutureChannel);
    	
    	singleBlockingChannel = ManagedChannelBuilder
                .forAddress(host, port)
                .intercept(new ClientInterceptorImpl(0))
                .usePlaintext()
                .build();
    	
    	singleBlockingStub = GreeterGrpc.newBlockingStub(singleFutureChannel);
    	sendsInProcess = new ArrayList<>();
    	
    }

    public GreetingClient(List<String> addresses) {
    	this.futureChannels = new ArrayList<>();
    	this.futureStubs = new ArrayList<>();
    	for (String address : addresses) {
    		String host = address.split(":")[0];
    		int port = Integer.parseInt(address.split(":")[1]);
    		ManagedChannel channel = ManagedChannelBuilder
                    .forAddress(host, port)
                    .intercept(new ClientInterceptorImpl(0))
                    .usePlaintext()
                    .build();
    		GreeterGrpc.GreeterFutureStub futureStub = GreeterGrpc.newFutureStub(channel);
    		futureChannels.add(channel);
    		futureStubs.add(futureStub);
    	}
    	
    	this.blockingChannels = new ArrayList<>();
    	this.blockingStubs = new ArrayList<>();
    	for (String address : addresses) {
    		String host = address.split(":")[0];
    		int port = Integer.parseInt(address.split(":")[1]);
    		ManagedChannel channel = ManagedChannelBuilder
                    .forAddress(host, port)
                    .intercept(new ClientInterceptorImpl(0))
                    .usePlaintext()
                    .build();
    		GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);
    		blockingChannels.add(channel);
    		blockingStubs.add(blockingStub);
    	}
    	
    	
    	
    }    
    
    public void shutdown() {
    	try {
	    	if(singleFutureChannel != null) {
	    		singleFutureChannel.shutdownNow();
	    	}
	    	for(ManagedChannel futureChannel : futureChannels) {
	    		if(futureChannel != null) {
	    			futureChannel.shutdownNow();
	        	}
	    	}
	    	if(singleBlockingChannel != null) {
	    		singleBlockingChannel.shutdownNow();
	    	}
	    	for(ManagedChannel blockingChannel : futureChannels) {
	    		if(blockingChannel != null) {
	    			blockingChannel.shutdownNow();
	        	}
	    	}
    	} catch(Exception e) {
    		
    	}
    }
    
    public boolean isAllFutureSendsDone() {
    	for(ListenableFuture<VoteReply> sendInProcess : sendsInProcess) {
    		if(!sendInProcess.isDone()) {
    			return false;
    		}
    	}
    	return true;
    }

    public List<ListenableFuture<VoteReply>> sendVoteFuture(app.models.Vote vote) { 
        VoteRequest req = VoteRequest.newBuilder()
                .setClientID(vote.getClientId())
                .setParty(vote.getParty())
                .setOriginState(vote.getOriginState())
                .setCurrentState(vote.getCurrentState())
                .setTimeStamp(vote.getTimeStamp())
                .build();
        List<ListenableFuture<VoteReply>> sendingsInProcess = new ArrayList<>();
        for (GreeterGrpc.GreeterFutureStub futureStub : futureStubs) {
        	ListenableFuture<VoteReply> rep = futureStub.receiveVote(req);
        	sendingsInProcess.add(rep);
        }
        this.sendsInProcess = sendingsInProcess;
        return sendingsInProcess;
    }
    
    public void sendVoteBlocking(app.models.Vote vote) { 
        VoteRequest req = VoteRequest.newBuilder()
                .setClientID(vote.getClientId())
                .setParty(vote.getParty())
                .setOriginState(vote.getOriginState())
                .setCurrentState(vote.getCurrentState())
                .setTimeStamp(vote.getTimeStamp())
                .build();
        VoteReply rep = singleBlockingStub.receiveVote(req);
    }
    
    
    
    private OneTimeUseElement<ListenableFuture<StartElectionsReply>> sendStartElections(StartElectionsRequest startElectionsRequest, GreeterGrpc.GreeterFutureStub futureStub) {
    	OneTimeUseElement<ListenableFuture<StartElectionsReply>> startReply = new OneTimeUseElement<>(futureStub.receiveStartElections(startElectionsRequest));
    	return startReply;
    }
    
    public void startElections() {
    	StartElectionsRequest startElectionsRequest = StartElectionsRequest.newBuilder().build();
    	List<OneTimeUseElement<ListenableFuture<StartElectionsReply>>> startElectionsFutures = new ArrayList<>();
    	for (GreeterGrpc.GreeterFutureStub futureStub : futureStubs) {
    		startElectionsFutures.add(sendStartElections(startElectionsRequest, futureStub));
		}
    	
    	int recievedCounter = 0;
    	while(recievedCounter < futureStubs.size()) {
    		for (OneTimeUseElement<ListenableFuture<StartElectionsReply>> startElectionsFuture : startElectionsFutures) {
    			if ((!startElectionsFuture.isUsed()) && (startElectionsFuture.getElement().isDone())) {
    				try {
    					StartElectionsReply startElectionsReply = startElectionsFuture.getElement().get();
    					startElectionsFuture.use();
    					recievedCounter++;
    				} catch(ExecutionException |  InterruptedException e) {
    					System.out.println("caught an exception in GreetingPaxosClient::sendVote while trying to get value of init future: " + e.getMessage());
    				}
    			}
    		}
    	}
    	return;
    }
    
    private OneTimeUseElement<ListenableFuture<EndElectionsReply>> sendEndElections(EndElectionsRequest endElectionsRequest, GreeterGrpc.GreeterFutureStub futureStub) {
    	OneTimeUseElement<ListenableFuture<EndElectionsReply>> endReply = new OneTimeUseElement<>(futureStub.receiveEndElections(endElectionsRequest));
    	return endReply;
    }
    
    public void endElections() {
    	EndElectionsRequest endElectionsRequest = EndElectionsRequest.newBuilder().build();
    	List<OneTimeUseElement<ListenableFuture<EndElectionsReply>>> endElectionsFutures = new ArrayList<>();
    	for (GreeterGrpc.GreeterFutureStub futureStub : futureStubs) {
    		endElectionsFutures.add(sendEndElections(endElectionsRequest, futureStub));
		}
    	int recievedCounter = 0;
    	while(recievedCounter < futureStubs.size()) {
    		for (OneTimeUseElement<ListenableFuture<EndElectionsReply>> endElectionsFuture : endElectionsFutures) {
    			if ((!endElectionsFuture.isUsed()) && (endElectionsFuture.getElement().isDone())) {
    				try {
    					EndElectionsReply endElectionsReply = endElectionsFuture.getElement().get();
    					endElectionsFuture.use();
    					recievedCounter++;
    				} catch(ExecutionException |  InterruptedException e) {
    					System.out.println("caught an exception in GreetingPaxosClient::sendVote while trying to get value of init future: " + e.getMessage());
    				}
    			}
    		}
    	}
    	return;
    	
    }
    
    public Map<VotesCountKey ,VotesCount> getAllVotesCounts(){
    	Map<VotesCountKey ,VotesCount> allVotesCounts = new HashMap<>();
    	VotesCountForPartyRequest votesCountForPartyRequest = VotesCountForPartyRequest.newBuilder().build();
    	for (GreeterGrpc.GreeterBlockingStub blockingStub : blockingStubs) {
    		Iterator<VotesCountForPartyReply> VotesCountForPartyReplyIterator = blockingStub.reciveVotesCount(votesCountForPartyRequest);
    		while(VotesCountForPartyReplyIterator.hasNext()) {
    			VotesCountForPartyReply votesCountForPartyReply = VotesCountForPartyReplyIterator.next();
    			String party = votesCountForPartyReply.getParty();
    			int count = votesCountForPartyReply.getVotesCount();
    			String state = votesCountForPartyReply.getState();
    			VotesCount votesCount = new VotesCount(party, count, state);
    			VotesCountKey key = new VotesCountKey(party, state);
    			VotesCount currentVotesCount = allVotesCounts.get(key);
    			if(currentVotesCount == null) {
    				allVotesCounts.put(key, votesCount);
    			} else {
    				currentVotesCount.add(count);
    			}
    		}
    	}
    	return allVotesCounts;
    }
    
    public VotesCount getStatus(String party, String state){

    	VotesCountForPartyRequest request = VotesCountForPartyRequest.newBuilder()
    			.setParty(party)
    			.setState(state)
    			.build();
    	VotesCountForPartyReply reply = this.singleBlockingStub.receiveStatus(request);
    	VotesCount votesCount = new VotesCount(reply.getParty(), reply.getVotesCount(), reply.getState());
    	return votesCount;
    }
    
    
    
    
}
