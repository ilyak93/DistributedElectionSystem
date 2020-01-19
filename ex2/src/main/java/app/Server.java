package app;

import org.apache.zookeeper.KeeperException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.common.util.concurrent.ListenableFuture;

import gRPCObjects.GreetingServer;
import gRPCObjects.paxos.GreetingPaxosServer;
import protos.Vote.VoteReply;
import zookeeper.ZkManager;
import gRPCObjects.GreetingClient;
import app.models.Distribution;
import app.models.Vote;
import app.models.VotesCount;
import app.models.VotesCountKey;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import app.controllers.VotesMap;
import app.readFiles.ReadStates;
import app.readFiles.ReadVoters;

@SpringBootApplication
public class Server {
	
	public static GreetingServer grpcServer;
	public static GreetingClient grpcClient;
	public static GreetingPaxosServer grpcPaxosServer;
	public static SpringApplication restServer;
	public static ZkManager zkManager;
	public static int serverId;
	public static AtomicInteger votesCounter = new AtomicInteger(0);
	public static  ConcurrentHashMap <Integer, Future<Vote>> votesInDistributionProcess = new ConcurrentHashMap<>();
	public static volatile boolean electionsStarted;
	public static volatile boolean electionsEnded;
	public static volatile boolean finishedAll = false;
	public static volatile int sendingRemoteVoteCounter = 0;
	public static volatile Integer sendingRemoteVoteMutex = new Integer(-1);
	public static volatile boolean receiveNewVotes = true;
	public static String state;
	public static Map<String, Integer> stateToElectors;
	public static Map<Integer, String> clientIdToOriginState;
	public static volatile boolean choseWinner;
	public static String winner;
	public static Set<String> allStates;
	
	/*
	 * @Param argv[0] = restServerPort
	 * @Param argv[1] = grpcServerPort
	 * @Param argv[2] = grpcPaxosServerPort
	 * @Param argv[3] = state
	 * @Param argv[4] = serverId
	 * @Param argv[5] = statesFile
	 * @param argv[6] = votersFile
	 * */
	public static void main(String argv[]) {
		electionsStarted = false;
		String restServerPort = argv[0];
		int grpcServerPort = Integer.parseInt(argv[1]);
		int grpcPaxosServerPort = Integer.parseInt(argv[2]);
		state = argv[3];
		serverId = Integer.parseInt(argv[4]);
		stateToElectors = new ReadStates(argv[5]).getStatesMap();
		clientIdToOriginState = new ReadVoters(argv[6]).getVotersMap();
		
		String localhost = "localhost";
		
		// create the ZooKeeper manager.
		String address = "127.0.0.1";
		int port = 2181;
		zkManager = new ZkManager(address, port, state, serverId, localhost, grpcPaxosServerPort, grpcServerPort);
		
		// Upload as REST server
		// Spring should be uploaded last.
		//the ZooKeeper initiate the server to listen on port 8080 by default. so the rest port need to be different.
		initilaizeRestServer(restServerPort, argv);
		
		// Upload as gRPC server
		GreetingServer server = initializeGrpcServer(serverId, grpcServerPort, state);
		
		// Upload as gRPC Paxos server
		GreetingPaxosServer grpcPaxosServer = initializeGreetingPaxosServer(serverId, grpcPaxosServerPort);
				
		pollElectionsStart();
		
		processFutureVotes();
		
		broadcastAllVotesInState();
		
		finishedAll = true;
		
		String winner = Server.getWinner();
		System.out.println("The winner is: " + winner);
	}
	
	private static GreetingServer initializeGrpcServer(int id, int port, String state) {
		grpcServer = new GreetingServer(id, port, state);
		System.out.println("Initialized gRPC server.");
		return grpcServer;
	}
	
	private static GreetingPaxosServer initializeGreetingPaxosServer(int id, int port) {
		grpcPaxosServer = new GreetingPaxosServer(id, port);
		System.out.println("Initialized gRPC Paxos server.");
		return grpcPaxosServer;
	}
	
	private static void initilaizeRestServer(String port, String argv[]) {
		restServer = new SpringApplication(Server.class);
		restServer.setDefaultProperties(Collections.singletonMap("server.port", port));
		restServer.run(argv);
		System.out.println("Initialized REST server.");
	}
	
	// deal with all votes that are done distributing by paxos.
	private static void processFutureVotes() {
		while(!electionsEnded || !zkManager.isAllFinishedRemoteSending() || !votesInDistributionProcess.isEmpty()) {
			//System.out.println("processFutureVotes");
			// Returns an iterator over the elements 
			Iterator<Entry<Integer,Future<Vote>>> iterator = votesInDistributionProcess.entrySet().iterator(); 
			// Printing the elements of the set 
			while (iterator.hasNext()) {
				Entry<Integer,Future<Vote>> entryFutureVote = iterator.next();
				if(entryFutureVote.getValue().isDone()) {
					synchronized(VotesMap.mutex){
						try {
							Vote vote = entryFutureVote.getValue().get();
							Vote currentVoteInMap = VotesMap.get(vote.getClientId());
							if((currentVoteInMap == null) || (vote.getTimeStamp() >= currentVoteInMap.getTimeStamp())) {
								VotesMap.put(vote.getClientId(), vote);
							}
							votesInDistributionProcess.remove(entryFutureVote.getKey());
							//iterator is no longer consistent.
							break;
						}catch( InterruptedException | ExecutionException e) {
							System.out.println("Got exception while handling future " + e.getMessage());
						} catch(Exception e) {
							
						}
						VotesMap.mutex.notify();
					}
				}
			}
		}
	}
	
	// busy wait until all servers started the elections
	//we assume that the preparation for the election start is enough time to power on all servers.
	private static void pollElectionsStart() {
		while(!electionsStarted || !zkManager.isStarted()) {
			if (electionsStarted) {
				zkManager.registerStartElections();
			}
			//System.out.println("Wait to start");
		}
		zkManager.printInitAndStartedChildren();
	}
	
	
	// broadcast all votes inside the state at the end of the elections;
	private static void broadcastAllVotesInState() {
		List<String> addressesInState = new ArrayList<>();
		try {
			addressesInState = zkManager.getCurrentStateAddressesForBroadcast();
		}catch(KeeperException | InterruptedException e) {
			
		}
		List<Vote> broadcastVotes = VotesMap.copyAll();
		int grpcId = 0;
		Map<Integer,GreetingClient> grpcClients = new HashMap<>();
		for(Vote vote : broadcastVotes) {
			GreetingClient grpcClient = new GreetingClient(addressesInState);
			grpcClient.sendVoteFuture(vote);
			grpcClients.put(grpcId, grpcClient);
			grpcId++;
		}
			
		while(grpcClients.size() != 0) {
			for (Entry<Integer,GreetingClient> grpcClient : grpcClients.entrySet()) {
				if(grpcClient.getValue().isAllFutureSendsDone()){
					try {
						grpcClient.getValue().shutdown();
					} catch (Exception e) {
						
					}
					grpcClients.remove(grpcClient.getKey());
					break;
				}
			}
		}
			
		boolean registeredEndBroadcast = false;
		while(!Server.zkManager.isEndedBroadcasts()) {
			if(!registeredEndBroadcast) {
				Server.zkManager.registerEndBroadcast();
				registeredEndBroadcast = true;
			}
			//System.out.println("Wait to end broadcast");
		}
	}
	
	public static String getWinner() {
		if(choseWinner) {
			return winner;
		}
		
		Map<VotesCountKey ,VotesCount> allVotesCountsMap = new HashMap<>();
    	
    	try {
    		GreetingClient grpcDistribution = new GreetingClient(Server.zkManager.getAddressInEachState());
    		allVotesCountsMap = grpcDistribution.getAllVotesCounts();
    		grpcDistribution.shutdown();
    	} catch(InterruptedException e) {
			System.out.println("Future interupted in countAllVotes: " + e.getMessage());
		} catch(KeeperException e) {
			System.out.println("Zookeeper exception incountAllVotes: " + e.getMessage());
		}
    	List<Distribution> distributions = VotesMap.getDistribution(allVotesCountsMap, Server.stateToElectors);
    	String winnerParty = "";
    	int maxElectors = 0;
    	for(Distribution distribution : distributions) {
    		if (distribution.getElectors() >= maxElectors) {
    			winnerParty = distribution.getParty();
    			maxElectors = distribution.getElectors();
    		}
    	}
    	choseWinner = true;
    	winner = winnerParty;
    	return winnerParty;
    	
	}
	
	
}
