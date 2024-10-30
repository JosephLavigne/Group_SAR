package task4.implementation;

import task4.Channel;

public class ChannelImplementation extends Channel{
	private ChannelManager channelManager;
	private CircularBuffer inBuffer;
	private CircularBuffer outBuffer;
	private boolean isDisconnected;
	
	// For debug
	private int port;
	private String brokerName;
	
	public ChannelImplementation(ChannelManager channelManager, CircularBuffer in, CircularBuffer out, int port, String brokerName) {
		this.channelManager = channelManager;
		this.inBuffer = in;
		this.outBuffer = out;
		this.isDisconnected = false;
		
		this.port = port;
		this.brokerName = brokerName;
	}

	@Override
	public int read(byte[] bytes, int offset, int length) throws ChannelDisconnectedException{
		// TODO Auto-generated method stub
		if(this.disconnected()) {
			throw new ChannelDisconnectedException();
		}
		else {
			int readedBytes = 0;
			try {
				while(readedBytes == 0) {
					if(this.inBuffer.empty()) {
						synchronized(this.inBuffer) {
							while(this.inBuffer.empty())
							{
								if(this.disconnected()) {
									throw new ChannelDisconnectedException();
								}
								else {
									try {
										this.inBuffer.wait();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
					else {
						while(readedBytes < length && this.inBuffer.empty()) {
							bytes[readedBytes + offset] = this.inBuffer.pull();
							readedBytes++;
						}
					}
					if(readedBytes != 0) {
						synchronized(this.inBuffer) {
							this.inBuffer.notify();
						}
					}
				}
			} catch (ChannelDisconnectedException e) {
				e.printStackTrace();
			}
			return readedBytes;
		}
	}

	@Override
	public int write(byte[] bytes, int offset, int length) throws ChannelDisconnectedException{
		// TODO Auto-generated method stub
		if(this.disconnected()) {
			throw new ChannelDisconnectedException();
		}
		else {
			int writedBytes = 0;
			try {
				while(writedBytes == 0) {
					if(this.outBuffer.full()) {
						synchronized(this.outBuffer) {
							while(this.outBuffer.full())
							{
								if(this.disconnected()) {
									throw new ChannelDisconnectedException();
								}
								else {
									try {
										this.outBuffer.wait();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
					else {
						while(writedBytes < length && this.inBuffer.empty()) {
							this.outBuffer.push(bytes[writedBytes + offset]);
							writedBytes++;
						}
					}
					if(writedBytes != 0) {
						synchronized(this.outBuffer) {
							this.outBuffer.notify();
						}
					}
				}
			} catch (ChannelDisconnectedException e) {
				e.printStackTrace();
			}
			return writedBytes;
		}
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		synchronized(this) {
			if(this.isDisconnected) {
				return;
			}
			this.isDisconnected = true;
			this.channelManager.disconnect(this);
		}
		synchronized(this.outBuffer) {
			this.outBuffer.notify();
		}
		synchronized(this.inBuffer) {
			this.inBuffer.notify();
		}
	}

	@Override
	public boolean disconnected() {
		// TODO Auto-generated method stub
		return this.isDisconnected && this.inBuffer.empty();
	}

}
