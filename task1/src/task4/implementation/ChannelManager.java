package task4.implementation;

import task4.Channel;
import task4.Channel.DisconnectListener;

public class ChannelManager {
	private ChannelImplementation connectChannel;
	private ChannelImplementation acceptChannel;
	
	private CircularBuffer acceptInBuffer;
	private CircularBuffer acceptOutBuffer;
	
	public ChannelManager() {
		this.connectChannel = new ChannelImplementation(this, acceptInBuffer, acceptOutBuffer);
		this.acceptChannel = new ChannelImplementation(this, acceptOutBuffer, acceptInBuffer);;
	}
	
	public Channel getConnectChannel() {
		return this.connectChannel;
	}
	
	public Channel getAcceptChannel() {
		return this.acceptChannel;
	}
	
	public void disconnect() {
		DisconnectListener disconnectListener = new DisconnectListener() {
			@Override
			public void disconnected() {
				//nothing
			}
		};
		this.acceptChannel.disconnect(disconnectListener);
		this.connectChannel.disconnect(disconnectListener);
	}
	
	public ChannelImplementation getRemoteChannel(Channel channel) {
		if (channel == this.connectChannel) {
			return this.acceptChannel;
		}
		else if (channel == this.acceptChannel) {
			return this.connectChannel;
		}
		else {
			return null;
		}
	}
}
