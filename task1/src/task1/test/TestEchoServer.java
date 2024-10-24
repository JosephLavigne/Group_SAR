package task1.test;
import java.util.ArrayList;
import java.util.List;

import task1.abstracts.Broker;
import task1.abstracts.Task;
import task1.implement.BrokerImplementation;
import task1.implement.BrokerManager;
import task1.implement.TaskImplementation;
import task1.test.Logger.LoggerLevel;

public class TestEchoServer {
	public static final LoggerLevel LOG_LEVEL = LoggerLevel.INFO;
	public static final int MESSAGES_TO_TEST = 5; 
	public static final int MAX_BROKER_TEST = 100;
	public static final String MESSAGE_TO_ECHO = "LA TERRE EST PLATE ET UN PLUS UN EGAL ONZE!";
	public static void main(String[] args) {
		testEchoServer();
	}
	
	public static void testEchoServer() {
		BrokerManager brokerManager = new BrokerManager();
		Broker brokerServer = new BrokerImplementation("BrokerServer", brokerManager);
		Broker brokerClient = new BrokerImplementation("BrokerClient", brokerManager);
		int port = 8080;
		simpleEchoServer(brokerServer, brokerClient, port);
	}
	
	public static void testEchoSameBroker() {
		BrokerManager brokerManager = new BrokerManager();
		Broker broker = new BrokerImplementation("Broker", brokerManager);
		int port = 8088;
		simpleEchoServer(broker, broker, port);
	}
	
	public static void testEchoMultipleBroker() {
		BrokerManager brokerManager = new BrokerManager();
		Broker brokerServer = new BrokerImplementation("BrokerServer", brokerManager);
		List<Task> tasks = new ArrayList<Task>();
		for(int i = 0; i < MAX_BROKER_TEST; i++) {
			Broker brokerClient = new BrokerImplementation("BrokerClient_" + i, brokerManager);
			
			Task taskServer = new TaskImplementation(brokerServer, new ServerTask(brokerServer, i));
			Task taskClient = new TaskImplementation(brokerClient, new ClientTask(brokerClient, i));
		}
		
		for (Task task : tasks) {
			try {
				task.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
		
	public static void simpleEchoServer(Broker brokerServer, Broker brokerClient, int port) {
		
		Task taskServer = new TaskImplementation(brokerServer, new ServerTask(brokerServer, port));
		
		Task taskClient = new TaskImplementation(brokerClient, new ClientTask(brokerClient, port));
		
		try {
			taskServer.join();
			taskClient.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
