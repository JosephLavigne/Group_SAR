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
	
	public Channel connect(Broker b, int port) {
		this.bConnect = b;
		Channel connectChannel = new ChannelImplementation(this.channelManager, this.clientInBuffer, this.clientOutBuffer, port, b.name);
		this.channelManager.setClient(connectChannel);
		if(this.bAccept != null) {
			notify();
		}
		else {
			while(this.bAccept == null) {
				try{
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return connectChannel;
	}
	
	public Channel accept(Broker b, int port) {
		this.bAccept = b;
		Channel acceptChannel = new ChannelImplementation(this.channelManager, this.clientOutBuffer, this.clientInBuffer, port, b.name);
		this.channelManager.setClient(acceptChannel);
		if(this.bConnect != null) {
			notify();
		}
		else {
			while(this.bConnect == null) {
				try{
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return acceptChannel;
	}
}
