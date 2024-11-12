package task4.implementation;

import task4.abs.Channel;
import task4.abs.Task;
import task4.implementation.runnables.WriteRunnable;

public class ChannelImplementation extends Channel{
	public ChannelManager channelManager;
	private CircularBuffer inBuffer;
	private CircularBuffer outBuffer;
	private boolean isDisconnected;
	private boolean dangling;
	
	private TaskImplementation writerTask;
	
	public ChannelImplementation(ChannelManager channelManager, CircularBuffer in, CircularBuffer out) {
		this.channelManager = channelManager;
		this.inBuffer = in;
		this.outBuffer = out;
		this.isDisconnected = false;
		this.dangling = false;
		this.writerTask = new TaskImplementation("Task Writer");
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
		this.writerTask.postRunnable(new WriteRunnable(bytes, offset, length, this.outBuffer, this, writeListener));
		return true;
	}

	@Override
	public void disconnect(DisconnectListener disconnectListener) {
		// TODO Auto-generated method stub
		ChannelImplementation remoteChannel = this.channelManager.getRemoteChannel(this);
		TaskImplementation disconnectTask = new TaskImplementation("Disconnect Task");
		disconnectTask.postRunnable(new Runnable() {
			@Override
			public void run() {
				isDisconnected = true;
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
