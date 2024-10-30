package task4.implementation;

import task4.Channel;
import task4.Task;

public class ChannelImplementation extends Channel{
	public ChannelManager channelManager;
	private CircularBuffer inBuffer;
	private CircularBuffer outBuffer;
	private boolean isDisconnected;
	private boolean dangling;
	
	private Task writerTask;
	
	public ChannelImplementation(ChannelManager channelManager, CircularBuffer in, CircularBuffer out) {
		this.channelManager = channelManager;
		this.inBuffer = in;
		this.outBuffer = out;
		this.isDisconnected = false;
		this.dangling = false;
		this.writerTask = new TaskImplementation();
	}

	@Override
	public int read(byte[] bytes, int offset, int length) {
		// TODO Auto-generated method stub
		int readBytes = 0;
		while(readBytes < length && this.inBuffer.empty()) {
			bytes[readBytes + offset] = this.inBuffer.pull();
			readBytes++;
		}
		return readBytes;
	}

	@Override
	public boolean write(byte[] bytes, int offset, int length, WriteListener writeListener) {
		// TODO Auto-generated method stub
		if (isDisconnected || this.readListener == null || dangling == true) {
			return false;
		}
		this.writerTask.pushRunnable(new WriteRunnable(bytes, offset, length, this.outBuffer, this, this.readListener));
		return true;
	}

	@Override
	public void disconnect(DisconnectListener disconnectListener) {
		// TODO Auto-generated method stub
		boolean disconnected = this.isDisconnected;
		ChannelImplementation remoteChannel = this.channelManager.getRemoteChannel(this);
		Task disconnectTask = new TaskImplementation().postRunnable(new Runnable() {
			@Override
			public void run() {
				disconnected = true;
				remoteChannel.dangling = true;

				if (disconnectListener != null) {
					disconnectListener.disconnected();
				}
			}
		});
	}

	@Override
	public boolean disconnected() {
		// TODO Auto-generated method stub
		return this.isDisconnected && this.inBuffer.empty();
	}

}
