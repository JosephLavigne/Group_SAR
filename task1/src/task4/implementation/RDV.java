package task4.implementation;

import task4.Broker;
import task4.Task;
import task4.implementation.BrokerImplementation.AcceptListener;
import task4.implementation.BrokerImplementation.ConnectListener;

public class RDV {
	private ChannelManager channelManager;
	
	private Task task;
	private AcceptRunnable acceptRunnable;
	private AcceptListener acceptListener;
	public boolean hasAcceptArrived;
	
	public RDV(AcceptListener acceptListener, int port) {
		this.channelManager = null;
		this.task = new TaskImplementation();
		this.acceptListener = acceptListener;
		this.acceptRunnable = new AcceptRunnable(this);
		this.hasAcceptArrived = false;
	}
	
	public void bind() {
		task.pushRunnable(acceptRunnable);
	}
	
	public void disconnect() {
		if (this.channelManager != null) {
			this.channelManager.disconnect();
		}
		task.finish();
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
