package zookeeper;

import java.io.IOException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;

/*
 * ZkConnector class is for the program to interact with ZooKeeper server
 * */

public class ZkConnector {
	private ZooKeeper zookeeper;
	java.util.concurrent.CountDownLatch connectedSignal = new java.util.concurrent.CountDownLatch(1);
	
	/*
	 *  Connects to the ZooKeeper server by passing a hosts argument.
	 * */
	public void connect(String host) throws IOException, InterruptedException {
	    zookeeper = new ZooKeeper(host, 5000, 
	                              new Watcher() {
	                                  public void process(WatchedEvent event) {
	                                      if (event.getState() == KeeperState.SyncConnected) {
	                                          connectedSignal.countDown();
	                                      }
	                                  }
	                              });
	                              connectedSignal.await();
	}
	
	/*
	 * Tells ZooKeeper server to shut down.
	 * */
	public void close() throws InterruptedException {
	    zookeeper.close();
	}
	
	/*
	 *  Gets the instance of live ZooKeeper server.
	 * */
	public ZooKeeper getZooKeeper() {
	    if (zookeeper == null || !zookeeper.getState().equals(States.CONNECTED)) {
	        throw new IllegalStateException("ZooKeeper is not connected.");
	    }
	    return zookeeper;
	}
}
