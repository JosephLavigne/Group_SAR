package task1.implementation;

import task1.Channel;

public class ChannelImplementation extends Channel{
	private ChannelManager channelManager;
	private CircularBuffer inBuffer;
	private CircularBuffer outBuffer;
	private boolean isDisconnected;
	
	public ChannelImplementation(ChannelManager channelManager, CircularBuffer in, CircularBuffer out) {
		this.channelManager = channelManager;
		this.inBuffer = in;
		this.outBuffer = out;
		this.isDisconnected = false;
	}

	@Override
	public int read(byte[] bytes, int offset, int length) {
		// TODO Auto-generated method stub
		if(this.disconnected()) {
			int readedBytes = 0;
			for(readedBytes = 0; readedBytes < length; readedBytes++) {
				if(!this.outBuffer.empty()) {
					bytes[readedBytes + offset] = this.inBuffer.pull();
				}
				else {
					return readedBytes;
				}
			}
			return readedBytes;
		}
		else {
			throw new IllegalStateException();
		}
	}

	@Override
	public int write(byte[] bytes, int offset, int length) {
		// TODO Auto-generated method stub
		if(this.disconnected()) {
			int sendedBytes = 0;
			for(sendedBytes = 0; sendedBytes < length; sendedBytes++) {
				if(!this.outBuffer.full()) {
					this.outBuffer.push(bytes[sendedBytes + offset]);
				}
				else {
					return sendedBytes;
				}
			}
			return sendedBytes;
		}
		else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		this.isDisconnected = true;
		this.outBuffer = new CircularBuffer(10);
		this.channelManager.disconnect(this);
	}

	@Override
	public boolean disconnected() {
		// TODO Auto-generated method stub
		return this.isDisconnected && this.inBuffer.empty();
	}

}
