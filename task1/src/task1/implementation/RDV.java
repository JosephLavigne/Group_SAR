package task1.implementation;

import task1.Broker;
import task1.Channel;

public class RDV {
	private Broker bAccept, bConnect;
	private ChannelManager channelManager;
	private CircularBuffer clientInBuffer;
	private CircularBuffer clientOutBuffer;
	
	public RDV() {
		this.bAccept = null;
		this.bConnect = null;
		this.channelManager = new ChannelManager();
		this.clientOutBuffer = new CircularBuffer(10);
		this.clientInBuffer = new CircularBuffer(10);
	}
	
	public synchronized Channel connect(Broker b) {
		if(this.bConnect == null) {
			this.bConnect = b;
			Channel connectChannel = new ChannelImplementation(this.channelManager, this.clientInBuffer, this.clientOutBuffer);
			this.channelManager.setClient(connectChannel);
			notifyAll();
			return connectChannel;
		}
		else {
			while(this.bAccept == null) {
				try{
					wait();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			} 
			this.bConnect = b;
			Channel connectChannel = new ChannelImplementation(this.channelManager, this.clientInBuffer, this.clientOutBuffer);
			this.channelManager.setClient(connectChannel);
			return connectChannel;
		}
	}
	
	public synchronized Channel accept(Broker b) {
		if(this.bAccept == null) {
			this.bAccept = b;
			Channel acceptChannel = new ChannelImplementation(this.channelManager, this.clientOutBuffer, this.clientInBuffer);
			this.channelManager.setServeur(acceptChannel);
			notifyAll();
			return acceptChannel;
		}
		else {
			while(this.bConnect == null) {
				try{
					wait();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			} 
			this.bAccept = b;
			Channel connectChannel = new ChannelImplementation(this.channelManager, this.clientInBuffer, this.clientOutBuffer);
			this.channelManager.setClient(connectChannel);
			return connectChannel;
		}
	}
}
