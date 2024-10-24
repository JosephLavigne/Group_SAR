package task1.test;

import task1.abstracts.Broker;
import task1.abstracts.Channel;

public class ServerTask implements Runnable{
	protected Broker serverBroker;
	protected int port;
	
	public ServerTask(Broker serverBroker, int port) {
		this.serverBroker = serverBroker;
		this.port = port;
	}

	@Override
	public void run() {
		String name = "taskServer_" + this.serverBroker.name + "_" + port;
		Logger.debug(name + "is accepting on port " + port + ".");
		Channel serverChannel = this.serverBroker.accept(port);
		MessageManager serverMessageManager = new MessageManager(serverChannel);
		if(serverChannel != null) {
			int treatedMessages = 0;
			while(treatedMessages < TestEchoServer.MESSAGES_TO_TEST) {
				String receivedMessage = serverMessageManager.readMessage();
				Logger.info(name + "received : " + receivedMessage);
				serverMessageManager.writeMessage(receivedMessage);
				treatedMessages++;
			}
			Logger.debug("Disconnecting  on port " + port + ".");
			serverChannel.disconnect();
		}
		else {
			Logger.error(name + " accept on port " + port + " returned null");
		}
	}

}
