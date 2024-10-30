package task4.implementation;

import task4.implementation.BrokerImplementation.AcceptListener;
import task4.implementation.BrokerImplementation.ConnectListener;
import task4.implementation.runnables.AcceptRunnable;

public class RDV {
	private ChannelManager channelManager;
	
	private TaskImplementation task;
	private AcceptRunnable acceptRunnable;
	private AcceptListener acceptListener;
	public boolean hasAcceptArrived;
	//For debug
	private int port;
	
	public RDV(AcceptListener acceptListener, int port) {
		this.channelManager = null;
		this.task = new TaskImplementation("Task : Accept on port : " + port);
		this.acceptListener = acceptListener;
		this.acceptRunnable = new AcceptRunnable(this);
		this.hasAcceptArrived = false;
		this.port = port;
	}
	
	public void bind() {
		task.postRunnable(acceptRunnable);
	}
	
	public void disconnect() {
		if (this.channelManager != null) {
			this.channelManager.disconnect();
		}
		task.kill();
	}
	
	public void accept() {
		this.acceptListener.accepted(this.channelManager.getAcceptChannel());
	}
	
	public void acceptConnect(ConnectListener connectListener) {
		if (this.hasAcceptArrived) {
			connectListener.refused();
		} else {
			this.hasAcceptArrived = true;
			this.channelManager = new ChannelManager();
			connectListener.connected(this.channelManager.getConnectChannel());
		}
	}
}
