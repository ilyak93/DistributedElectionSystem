package gRPCObjects.paxos;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import app.models.Vote;


public class FuturePaxosGreetingClient {

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public Future<Vote> calculate(List<String> addresses, int serverId, Vote vote) {        
        return executor.submit(() -> {
        	GreetingPaxosClient greetingPaxosClient = new GreetingPaxosClient(addresses, serverId);
        	Vote updatedVote = greetingPaxosClient.sendVote(vote);
        	greetingPaxosClient.shutdown();
        	return updatedVote;
        });
    }
}
