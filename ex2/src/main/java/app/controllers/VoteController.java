package app.controllers;

import app.Server;

import app.models.Vote;
import app.models.VotesCount;
import app.models.VotesCountKey;
import app.models.CommandElections;
import app.models.ControllerMessage;
import app.models.Distribution;
import gRPCObjects.GreetingClient;
import gRPCObjects.paxos.FuturePaxosGreetingClient;
import org.apache.zookeeper.KeeperException;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/*

All the curl commands supported by the REST Controller in our project:

@GetMapping("/votes")
curl -X GET localhost:8081/votes

@GetMapping("/votes/{clientId}")
curl -X GET localhost:8081/votes/1

@GetMapping("/count_votes_in_state")
curl -X GET localhost:8081/count_votes_in_state

@GetMapping("/count_all_votes")
curl -X GET localhost:8081/count_all_votes

@GetMapping("/single_state_party_status")
curl -X GET localhost:8081/single_state_party_status

@GetMapping("/distribution")
curl -X GET localhost:8081/distribution

@GetMapping("/winner")
curl -X GET localhost:8081/winner

@PostMapping("/votes")
curl -X POST localhost:8081/votes -H "Content-Type:Application/json" -d {\"clientId\":1,\"party\":\"Republicans\"}

@PostMapping("/commandElections")
curl -X POST localhost:8081/commandElections -H "Content-Type:Application/json" -d {\"command\":\"start\"}
curl -X POST localhost:8081/commandElections -H "Content-Type:Application/json" -d {\"command\":\"end\"}
curl -X POST localhost:8081/commandElections -H "Content-Type:Application/json" -d {\"command\":\"isfinished\"}

*/



@RestController
public class VoteController {

	VoteController() {}
    
    @GetMapping("/votes")
    List<Vote> all() {
        return new ArrayList<>(VotesMap.getAll());
    }
    
    @GetMapping("/votes/{clientId}")
    //Resource<Vote> one(@PathVariable int clientId) {
    ControllerMessage one(@PathVariable int clientId) {
    	ControllerMessage controllerMessage = new ControllerMessage("");
        Vote vote = VotesMap.get(clientId);
        if(vote == null) {
        	controllerMessage.setMessage("The client " + clientId + "is not saved in this server.\n try another server or try later.");
        	return null;
        } else {
        	controllerMessage.setMessage(vote.toString());
        }
        return controllerMessage;
        
        /*
        return new Resource<Vote>(vote,
                linkTo(methodOn(VoteController.class).one(clientId)).withSelfRel(),
                linkTo(methodOn(VoteController.class).all()).withRel("votes"));
                */
    }
    
    @GetMapping("/count_votes_in_state")
    List<VotesCount> countVotesInState(){
    	return new ArrayList<>(VotesMap.countVotes().values());
    }
    
    @GetMapping("/count_all_votes")
    List<VotesCount> countAllVotes(){
    	List<VotesCount> allVotesCounts = new ArrayList<>();
    	
    	try {
    		GreetingClient grpcEndElections = new GreetingClient(Server.zkManager.getAddressInEachState());
    		allVotesCounts = new ArrayList<>(grpcEndElections.getAllVotesCounts().values());
    		grpcEndElections.shutdown();
    	} catch(InterruptedException e) {
			System.out.println("Future interupted in countAllVotes: " + e.getMessage());
		} catch(KeeperException e) {
			System.out.println("Zookeeper exception incountAllVotes: " + e.getMessage());
		} catch(Exception e) {
			
		}
    	return allVotesCounts;
    }
    
    @GetMapping("/single_state_party_status")
    VotesCount getSingleStatePartyStatus(@RequestBody VotesCountKey key) {
    	VotesCount status = null;
    	try {
    		GreetingClient grpcStatus = new GreetingClient(Server.zkManager.getAddressInAnotherState(key.getState()));
    		status = grpcStatus.getStatus(key.getParty(), key.getState());
    		grpcStatus.shutdown();
    	} catch(InterruptedException e) {
			System.out.println("Future interupted in getSingleStatePartyStatus: " + e.getMessage());
		} catch(KeeperException e) {
			System.out.println("Zookeeper exception getSingleStatePartyStatus: " + e.getMessage());
		} catch(Exception e) {
			System.out.println("caught unknown exception " + e.getMessage());
		}
    	return status;
    }
    
    @GetMapping("/distribution")
    List<Distribution> getDistribution(){
    	Map<VotesCountKey ,VotesCount> allVotesCountsMap = new HashMap<>();
    	
    	try {
    		GreetingClient grpcDistribution = new GreetingClient(Server.zkManager.getAddressInEachState());
    		allVotesCountsMap = grpcDistribution.getAllVotesCounts();
    		grpcDistribution.shutdown();
    	} catch(InterruptedException e) {
			System.out.println("Future interupted in countAllVotes: " + e.getMessage());
		} catch(KeeperException e) {
			System.out.println("Zookeeper exception incountAllVotes: " + e.getMessage());
		} catch(Exception e) {
			
		}
    	List<Distribution> distributions = VotesMap.getDistribution(allVotesCountsMap, Server.stateToElectors);
    	return distributions;
    }
    
    @GetMapping("/winner")
    ControllerMessage getWinner()
    {
    	ControllerMessage controllerMessage = new ControllerMessage("The winner is still unknown");
    	if(Server.choseWinner) {
    		String winner = Server.getWinner();
    		controllerMessage.setMessage("The winner is: " + winner);
    	}
    	return controllerMessage;
    }
    
    @PostMapping("/votes")
    ControllerMessage newVote(@RequestBody Vote newVote) {
    	
    	ControllerMessage controllerMessage = new ControllerMessage("");
    	
    	if(Server.electionsEnded) {
    		controllerMessage.setMessage("Elections ended. Cannot receive new votes.");
    		return controllerMessage;
    	}
    	if(!newVote.isValid()) {
    		controllerMessage.setMessage("the client ID does not appear in the voters registration file.");
    		return controllerMessage;
    	}
    	
    	
    	if(!Server.electionsStarted || !Server.receiveNewVotes || Server.electionsEnded) {
    		controllerMessage.setMessage("Out of elections time. Cannot receive new votes.");
    		return controllerMessage;
    	}
    	
    	if (newVote.getOriginState().compareTo(newVote.getCurrentState()) != 0) {
    		try {
    			synchronized(Server.sendingRemoteVoteMutex){
    				Server.sendingRemoteVoteCounter++;
    				if(Server.electionsEnded) {
    		    		controllerMessage.setMessage("Elections ended. Cannot receive new votes.");
    		    		Server.sendingRemoteVoteCounter--;
    		    		Server.sendingRemoteVoteMutex.notifyAll();
    		    		return controllerMessage;
    		    	}
    				Server.sendingRemoteVoteMutex.notifyAll();
    			}
    			String originStateAddresses = Server.zkManager.getAddressInAnotherState(newVote.getOriginState());
    			GreetingClient greetingClient = new GreetingClient(originStateAddresses);
    			greetingClient .sendVoteBlocking(newVote);
    			greetingClient.shutdown();
    			
    			synchronized(Server.sendingRemoteVoteMutex){
    				Server.sendingRemoteVoteCounter--;
    				Server.sendingRemoteVoteMutex.notifyAll();
    			}
    		} catch(InterruptedException e) {
    			System.out.println("Future interupted in @PostMapping newVote: " + e.getMessage());
    		} catch(KeeperException e) {
    			System.out.println("Zookeeper exception in @PostMapping newVote: " + e.getMessage());
    		}
    	}
    	else {
    		try {
    		Future<Vote> future = new FuturePaxosGreetingClient().calculate(Server.zkManager.getCurrentStateAddressesForPaxos(), Server.serverId, newVote);
    		int voteNumber = Server.votesCounter.addAndGet(1);
    		Server.votesInDistributionProcess.put(voteNumber, future);
    		} catch(InterruptedException e) {
    			System.out.println("Future interupted in @PostMapping newVote: " + e.getMessage());
    		} catch(KeeperException e) {
    			System.out.println("Zookeeper exception in @PostMapping newVote: " + e.getMessage());
    		}
    	}
    	controllerMessage.setMessage("The Elections system received the vote Successfully\n" 
    			+ "clientID: " + newVote.getClientId() + "\n" 
    			+ "party: " + newVote.getParty() + "\n"
    			+ "current state: " + newVote.getCurrentState() + "\n"
    			+ "origin state: " + newVote.getOriginState() + "\n"
    			+ "timestamp: " + new Date(newVote.getTimeStamp()) + "\n");
		return controllerMessage;
    }
    
   
    
    @PostMapping("/commandElections")
    ControllerMessage startElections(@RequestBody CommandElections commandElections){
    	ControllerMessage controllerMessage = new ControllerMessage("Unknown controller command");
    	String command = commandElections.getCommand();
    	if(command.equals("start")) {
    		if(Server.electionsStarted) {
    			controllerMessage.setMessage("Elections allready started");
    		}
    		else if(!Server.electionsStarted && Server.electionsEnded) {
	    		controllerMessage.setMessage("Elections ended before they started");
	    	} else {
		    	GreetingClient grpcStartElections = new GreetingClient(Server.zkManager.getAddressesToStartElections());
		    	grpcStartElections.startElections();
		    	grpcStartElections.shutdown();
		    	controllerMessage.setMessage("Started elections");
	    	}
    	}
    	if(command.equals("end")) {
    		if(Server.receiveNewVotes) {
	    		Server.receiveNewVotes = false;
	    		GreetingClient grpcEndElections = new GreetingClient(Server.zkManager.getAddressesToStartElections());
	        	grpcEndElections.endElections();
	        	grpcEndElections.shutdown();
    		}
    		controllerMessage.setMessage("Endeded elections");
    	}
    	if(command.equals("isfinished")) {
    		if(Server.finishedAll) {
    			controllerMessage.setMessage("Finished all counting");
    		} else {
    			controllerMessage.setMessage("Still in process");
    		}
    	}
    	if(command.equals("isStarted")) {
    		if(Server.electionsStarted) {
    			controllerMessage.setMessage("Elections started");
    		} else {
    			controllerMessage.setMessage("Elections not started");
    		}
    	}
    	if(command.equals("isEnded")) {
    		if(Server.electionsEnded) {
    			controllerMessage.setMessage("Elections ended");
    		} else {
    			controllerMessage.setMessage("Elections not ended");
    		}
    	}
    	return controllerMessage;
    }
}
