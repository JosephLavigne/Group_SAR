package task4.implementation;

import task4.Channel;

public class ChannelManager {
	private Channel connectChannel;
	private Channel acceptChannel;
	
	private CircularBuffer acceptInBuffer;
	private CircularBuffer acceptOutBuffer;
	
	public ChannelManager() {
		this.connectChannel = new Channel(this, acceptInBuffer, acceptOutBuffer);
		this.acceptChannel = new Channel(this, acceptOutBuffer, acceptInBuffer);;
	}
	
	public Channel getConnectChannel() {
		return this.connectChannel;
	}
	
	public Channel getAcceptChannel() {
		return this.acceptChannel;
	}
	
	public void disconnect() {
		this.acceptChannel.disconnect();
		this.connectChannel.disconnect();
	}
}
