package zookeeper;

import java.io.IOException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZkSync implements Watcher {
	
		protected Integer mutex;
	    protected ZooKeeper zk = null;
	    protected String root;

	    public ZkSync(String address) {
	        if(zk == null){
	            try {
	                System.out.println("Starting ZK:");
	                //the ZooKeeper initiate the server to listen on port 8080 by default.
	                zk = new ZooKeeper(address, 3000, this);
	                mutex = new Integer(-1);
	                System.out.println("Finished starting ZK: " + zk);
	            } catch (IOException e) {
	                System.out.println(e.toString());
	                zk = null;
	            }
	        }
	    }

	    synchronized public void process(WatchedEvent event) {
	        synchronized (mutex) {
	            mutex.notify();
	        }
	    }
}
