# DistributedElectionSystem
implemented in Java using gRPC implementing Paxos, REST and ZooKeeper


Run Instructions:

1. Unzip the archive anywhere you like
2. Download the ZooKeeper bin ver. from following link:
   https://www.apache.org/dist/zookeeper/zookeeper-3.5.6/
   and run the ZooKeeper by starting zkServer.cmd from the shell from bin directory: apache-zookeeper-3.5.6-bin\bin\zkServer.cmd

3. Run from the eclipse 3 runs (different servers) with the following run configurations:
 
 3.1 open Run Configurations -> New Launch Configuration -> change Project: ex2 MainClass: app.Server
 	in Arguments -> Program argments put:
8081 8082 8083 Texas 0 <YourRelativePath>/ex2.1/tests/states.csv <YourRelativePath>/ex2.1/tests/voters.csv
 
 3.2 same as for 3.1 with the following arguments:
8084 8085 8086 Texas 1  <YourRelativePath>/ex2.1/tests/states.csv <YourRelativePath>/ex2.1/tests/voters.csv
 
 3.3 same as for 3.1 with the following arguments:
8087 8088 8089 Texas 2  <YourRelativePath>/ex2.1/tests/states.csv <YourRelativePath>/ex2.1/tests/voters.csv

4. all the possible REST commands are in the "supportedRESTcommands.txt" file, run any of them in new shell (curl lines with their corresponding method annotation from the java source)

5. U can use the test.cmd file, drag it to the shell and Enter
