package zookeeper;

import java.io.IOException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import zookeeper.ZkConnector;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.junit.jupiter.api.Test;

public class ZkConnectTest {
    public void main() {
		// Declare a new ZooKeeper variable
		ZooKeeper zk;
		//Create a new instance of the ZkConnector class
		ZkConnector zkc = new ZkConnector();
		try {
			//Connect to the zookeeper
			zkc.connect("localhost");
			//Let zk be the instance of the zookeeper connected
			zk = zkc.getZooKeeper();
			//Create a new znode, just to see if it worked correctly.
			zk.create("/newznode", "new znode".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch(Exception e) {
			System.out.println("caught exception: " + e.getMessage());
		}
		
	}
}
