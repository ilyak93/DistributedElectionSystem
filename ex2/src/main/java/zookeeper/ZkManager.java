package zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;


public class ZkManager extends ZkSync {
		
		private int serverIndex;
		private String state;
		private String nodePath;
		private String initPath = "/init";
		private String electionsStartPath = "/electionsStart";
		private String finishedRemoteSending = "/finishedRemoteSending";
		private String endBroadcast;
		
		public ZkManager(String address, int port, String state, int serverIndex, String localhost, int grpcPaxosServerPort, int grpcServerPort) {
			super(address);
		    this.root = "/" + state + "/servers";
		    // Create ZK node state
		    if (zk != null) {
		    	String pathElements[] =  this.root.split("/");
		    	System.out.println("pathElement " + pathElements[0]);
		    	System.out.println("pathElement " + pathElements[1]);
		    	System.out.println("pathElement " + pathElements[2]);
		    	String path = "";
		        try {
		        	for(int i = 1; i < pathElements.length; i++) {
		        		path += "/" + pathElements[i];
		        		System.out.println("Path is: " + path);
			            Stat s = zk.exists(path, false);
			            if (s == null) {
			            	zk.create(path, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			                System.out.println("Created node " + path);
			            }
		        	}
		        	String data = localhost + ":" + grpcPaxosServerPort;
		            this.nodePath = zk.create(root + "/" + serverIndex, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		            System.out.println("Created node " + this.nodePath);
		        } catch (KeeperException e) {
		            System.out.println("ZooKeeper exception: " + e.toString());
		        } catch (InterruptedException e) {
		            System.out.println("Interrupted exception" + e.toString());
		        }
		        try {
			        Stat s = zk.exists(initPath, false);
		            if (s == null) {
		            	zk.create(initPath, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		                System.out.println("Created node " + initPath);
		            }
		            String data = localhost + ":" + grpcServerPort;
		            zk.create(initPath + "/" + state + serverIndex , data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		        } catch (KeeperException e) {
		            System.out.println("ZooKeeper exception: " + e.toString());
		        } catch (InterruptedException e) {
		            System.out.println("Interrupted exception" + e.toString());
		        }
		        try {
			        Stat s = zk.exists(electionsStartPath, false);
		            if (s == null) {
		            	zk.create(electionsStartPath, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		                System.out.println("Created node " + electionsStartPath);
		            }
		        } catch (KeeperException e) {
		            System.out.println("ZooKeeper exception: " + e.toString());
		        } catch (InterruptedException e) {
		            System.out.println("Interrupted exception" + e.toString());
		        }
		        try {
			        Stat s = zk.exists("/" + state +"/endBroadcast", false);
		            if (s == null) {
		            	endBroadcast = zk.create("/" + state +"/endBroadcast", new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		                System.out.println("Created node " + endBroadcast);
		            } else {
		            	endBroadcast = "/" + state +"/endBroadcast";
		            }
		        } catch (KeeperException e) {
		            System.out.println("ZooKeeper exception: " + e.toString());
		        } catch (InterruptedException e) {
		            System.out.println("Interrupted exception" + e.toString());
		        }
		        try {
			        Stat s = zk.exists(finishedRemoteSending, false);
		            if (s == null) {
		            	zk.create(finishedRemoteSending, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		                System.out.println("Created node " + endBroadcast);
		            }
		        } catch (KeeperException e) {
		            System.out.println("ZooKeeper exception: " + e.toString());
		        } catch (InterruptedException e) {
		            System.out.println("Interrupted exception" + e.toString());
		        }
		    }
		    this.serverIndex = serverIndex;
		    this.state = state;
		}
		
		public List<String> getLiveServers()  throws KeeperException, InterruptedException{
			List<String> liveServers = new ArrayList<>();
			Stat s = zk.exists(this.root, false);
			if (s == null) {
				System.out.println("Cannot find the root node");
			} else {
				liveServers = zk.getChildren(this.root, true);
			}
			return liveServers;
		}
		
		public List<String> getCurrentStateAddressesForPaxos() throws KeeperException, InterruptedException {
			
			Stat stat = zk.exists(this.root, false);
			if (stat == null) {
				System.out.println("Cannot find the root node");
			} 
			List<String> liveServers = getLiveServers();
			List<String> liveServersAddresses = new ArrayList<>();
			for (String liveServer : liveServers) {
				byte[] addressBytes = zk.getData(this.root + "/" + liveServer, false, stat);
				String address = new String(addressBytes);
				liveServersAddresses.add(address);
			}
			return liveServersAddresses;
		}
		
		public List<String> getCurrentStateAddressesForBroadcast() throws KeeperException, InterruptedException {
			List<String> liveServers = new ArrayList<>();
			List<String> addresses = new ArrayList<>();
			Stat stat = zk.exists(initPath, false);
			if (stat == null) {
				System.out.println("Cannot find the init node");
			} else {
				System.out.println("Found the init node");
				liveServers = zk.getChildren(initPath, true);
				for(String server : liveServers) {
					if(server.contains(state)) {
						byte[] addressBytes = zk.getData("/init/" + server, false, stat);
						String address = new String(addressBytes);
						addresses.add(address);
					}
				}
			}
			return addresses;
		}
		
		public int getServerIndex() {
			return this.serverIndex;
		}
		
		public String getState() {
			return this.state;
		}
		
		public String getAddressInAnotherState(String anotherState) throws KeeperException, InterruptedException{
			List<String> liveServers = new ArrayList<>();
			Stat stat = zk.exists("/init", false);
			if (stat == null) {
				System.out.println("Cannot find the init node");
			} else {
				System.out.println("Found the init node");
				liveServers = zk.getChildren(this.initPath, true);
				for(String server : liveServers) {
					if(server.contains(anotherState)) {
						byte[] addressBytes = zk.getData("/init/" + server, false, stat);
						String address = new String(addressBytes);
						return address;
					}
				}
			}
			return null;
		}
		
		public List<String> getAddressInEachState() throws KeeperException, InterruptedException {
			Map<String, String> addressesInStates = new HashMap<>();
			List<String> liveServers = new ArrayList<>();
			Stat stat = zk.exists(initPath, false);
			if (stat == null) {
				System.out.println("Cannot find the init node");
			} else {
				System.out.println("Found the init node");
				liveServers = zk.getChildren(initPath, true);
				for(String server : liveServers) {
					String stateOfServer = server.replaceAll("[0-9]","");
					if(!addressesInStates.containsKey(stateOfServer)) {
						byte[] addressBytes = zk.getData(initPath + "/" + server, false, stat);
						String address = new String(addressBytes);
						addressesInStates.put(stateOfServer, address);
					}
				}
			}
			List<String> addresses = new ArrayList<>(addressesInStates.values());
			return addresses;
		}
		
		
		public void registerStartElections() {
			try {
		        Stat s = zk.exists(electionsStartPath, false);
	            if (s == null) {
	            	zk.create(electionsStartPath, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	                System.out.println("Created node " + electionsStartPath);
	            }
	            s = zk.exists(electionsStartPath + "/" + state + serverIndex, false);
	            if (s == null) {
	            	zk.create(electionsStartPath + "/" + state + serverIndex , new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	            }
	        } catch (KeeperException e) {
	            System.out.println("ZooKeeper exception: " + e.toString());
	        } catch (InterruptedException e) {
	            System.out.println("Interrupted exception" + e.toString());
	        }
		}
		
		
		public boolean isStarted() {
			boolean started = false;
			try {
				Stat startedServersStat = zk.exists(this.electionsStartPath, false);
				Stat upServersStat =  zk.exists(this.initPath, false);
				if ((startedServersStat == null) || (upServersStat == null)) {
					System.out.println("Cannot find the electionsStartPath or init node");
				} else {
					List<String> startedServers = zk.getChildren(this.electionsStartPath, true);
					List<String> upServers = zk.getChildren(this.initPath, true);
					started = startedServers.size() == upServers.size();
				}
			} catch (KeeperException e) {
				System.out.println("ZooKeeper exception: " + e.toString());
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception" + e.toString());
			} catch (Exception e) {
				System.out.println("Unknown exception" + e.getMessage());
			}
			return started;
		}
		
		public void printInitAndStartedChildren() {
			try {
				Stat startedServersStat = zk.exists(this.electionsStartPath, false);
				Stat upServersStat =  zk.exists(this.initPath, false);
				if ((startedServersStat == null) || (upServersStat == null)) {
					System.out.println("Cannot find the electionsStartPath or init node");
				} else {
					List<String> startedServers = zk.getChildren(this.electionsStartPath, true);
					List<String> upServers = zk.getChildren(this.initPath, true);
					System.out.println("electionsStartPath children " + startedServers.size());
					System.out.println("initPath children " + upServers.size());
				}
			} catch (KeeperException e) {
				System.out.println("ZooKeeper exception: " + e.toString());
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception" + e.toString());
			} catch (Exception e) {
				System.out.println("Unknown exception" + e.getMessage());
			}
		}
		
		
		public List<String> getAddressesToStartElections(){
			List<String> allLiveServers = new ArrayList<>();
			List<String> liveServersAddresses = new ArrayList<>();
			try {
				Stat allLiveServersStat =  zk.exists(this.initPath, false);
				if(allLiveServersStat == null) {
					System.out.println("Cannot find the electionsStartPath node");
				} else {
					allLiveServers = zk.getChildren(initPath, true);
				}
				for (String liveServer : allLiveServers) {
					byte[] addressBytes = zk.getData(this.initPath + "/" + liveServer, false, allLiveServersStat);
					String address = new String(addressBytes);
					liveServersAddresses.add(address);
				}
			} catch (KeeperException e) {
				System.out.println("ZooKeeper exception: " + e.toString());
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception" + e.toString());
			}
			return liveServersAddresses;
		}
		
		public void registerFinishedRemoteSending() {
			try {
		        Stat s = zk.exists(finishedRemoteSending, false);
	            if (s == null) {
	            	zk.create(finishedRemoteSending, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	                System.out.println("Created node " + electionsStartPath);
	            }
	            s = zk.exists(finishedRemoteSending + "/" + state + serverIndex, false);
	            if (s == null) {
	            	zk.create(finishedRemoteSending + "/" + state + serverIndex , new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	            }
	        } catch (KeeperException e) {
	            System.out.println("ZooKeeper exception: " + e.toString());
	        } catch (InterruptedException e) {
	            System.out.println("Interrupted exception" + e.toString());
	        }
		}
		
		//check if all the servers in the system finished to send remote votes.
		public boolean isAllFinishedRemoteSending() {
			boolean finished = false;
			try {
				Stat finishedRemoteSendingStat = zk.exists(this.finishedRemoteSending, false);
				Stat upServersStat =  zk.exists(this.initPath, false);
				if ((finishedRemoteSendingStat == null) || (upServersStat == null)) {
					System.out.println("Cannot find the finishedRemoteSending node");
				} else {
					List<String> finishedRemoteSendings = zk.getChildren(this.finishedRemoteSending, true);
					List<String> upServers = zk.getChildren(this.initPath, true);
					finished = finishedRemoteSendings.size() == upServers.size();
				}
			} catch (KeeperException e) {
				System.out.println("ZooKeeper exception: " + e.toString());
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception" + e.toString());
			}
			return finished;
		}
		
		public void registerEndBroadcast() {
			try {
		        Stat s = zk.exists(endBroadcast, false);
	            if (s == null) {
	            	zk.create(endBroadcast, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	                System.out.println("Created node " + endBroadcast);
	            }
	            zk.create(endBroadcast + "/"  + serverIndex , new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	        } catch (KeeperException e) {
	            System.out.println("ZooKeeper exception: " + e.toString());
	        } catch (InterruptedException e) {
	            System.out.println("Interrupted exception" + e.toString());
	        }
		}
		
		public boolean isEndedBroadcasts() {
			boolean ended = false;
			try {
				Stat endBroadcastStat = zk.exists(this.endBroadcast, false);
				Stat upServersInStateStat =  zk.exists(this.root, false);
				if ((endBroadcastStat == null) || (upServersInStateStat == null)) {
					System.out.println("Cannot find the electionsStartPath node");
				} else {
					System.out.println("Found the endBroadcast node");
					List<String> endedBroadcasts = zk.getChildren(this.endBroadcast, true);
					List<String> upServersInState = zk.getChildren(this.root, true);
					ended = endedBroadcasts.size() == upServersInState.size();
				}
			} catch (KeeperException e) {
				System.out.println("ZooKeeper exception: " + e.toString());
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception" + e.toString());
			}
			return ended;
		}
		
}
