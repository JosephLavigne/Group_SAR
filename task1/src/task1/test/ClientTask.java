package task1.test;

import task1.abstracts.Broker;
import task1.abstracts.Channel;

public class ClientTask implements Runnable{
	protected Broker clientBroker;
	protected int port;
	
	public ClientTask(Broker clientBroker, int port) {
		this.clientBroker = clientBroker;
		this.port = port;
	}

	@Override
	public void run() {
		String name = "taskClient_" + this.clientBroker.name + "_" + port;
		Logger.debug(name + "is connecting on port " + port + " of broker " + this.clientBroker.name + ".");
		Channel clientChannel = this.clientBroker.connect("BrokerServer", port);
		MessageManager clientMessageManager = new MessageManager(clientChannel);
		if(clientChannel != null) {
			int messageSended = 0;
			while(messageSended < TestEchoServer.MESSAGES_TO_TEST) {
				clientMessageManager.writeMessage(TestEchoServer.MESSAGE_TO_ECHO);
				String echoString = clientMessageManager.readMessage();
				if(!echoString.equals(TestEchoServer.MESSAGE_TO_ECHO)) {
					Logger.info("TEST FAILED on " + name + " sended : " + TestEchoServer.MESSAGE_TO_ECHO + " and received : " + echoString);
				}
				messageSended++;
			}
			Logger.debug("Disconnecting  on port " + port + ".");
			clientChannel.disconnect();
		}
		else {
			Logger.error(name + " connect on port " + port + " of broker " + this.clientBroker.name + " returned null");
		}
	}
}
