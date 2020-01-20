package gRPCObjects;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.zookeeper.KeeperException;

import app.Server;
import app.models.Vote;


public class FutureRemoteGreetingClient {

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public Future<Vote> calculate(Vote vote) {        
        return executor.submit(() -> {
        	try {
        		synchronized (app.Server.sendingRemoteVoteMutex) {
        			Server.sendingRemoteVoteCounter++;
        		}
	        	//int i = Server.sendingRemoteVoteCounter.addAndGet(1);
	        	String originStateAddress = Server.zkManager.getAddressInAnotherState(vote.getOriginState());
				GreetingClient greetingClient = new GreetingClient(originStateAddress);
				greetingClient.sendVoteBlocking(vote);
				//greetingClient.shutdown();
				synchronized (app.Server.sendingRemoteVoteMutex) {
        			app.Server.sendingRemoteVoteCounter--;
        		}
				//int j = Server.sendingRemoteVoteCounter.decrementAndGet();
				//int u = 9;
        		
        	} catch(InterruptedException e) {
    			System.out.println("Future interupted in @PostMapping newVote: " + e.getMessage());
    		} catch(KeeperException e) {
    			System.out.println("Zookeeper exception in @PostMapping newVote: " + e.getMessage());
    		} catch (Exception e) {
				System.out.println("Caught exception " + e.getMessage());
			}
        	return vote;
        });
    }
}
