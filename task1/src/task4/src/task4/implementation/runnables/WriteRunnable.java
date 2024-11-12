package task4.implementation.runnables;

import task4.abs.Channel;
import task4.abs.Channel.WriteListener;
import task4.implementation.ChannelImplementation;
import task4.implementation.CircularBuffer;

public class WriteRunnable implements Runnable{
	
	private byte[] bytes;
	private int offset;
	private int length;
	private CircularBuffer outBuffer;
	private Channel channel;
	private WriteListener writeListener;
	private ChannelImplementation remoteChannel;

	public WriteRunnable(byte[] bytes, int offset, int length, CircularBuffer outBuffer, ChannelImplementation channel, WriteListener writeListener) {
		this.bytes = bytes;
		this.offset = offset;
		this.length = length;
		this.outBuffer = outBuffer;
		this.channel = channel;
		this.writeListener = writeListener;
		this.remoteChannel = channel.channelManager.getRemoteChannel(channel);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (this.channel.disconnected())
			return;

		int bytesWritten = 0;
		while (bytesWritten < length && !this.outBuffer.full()) {
			byte value = bytes[offset + bytesWritten];
			this.outBuffer.push(value);
			bytesWritten++;
		}

		this.writeListener.writtenBytes(bytesWritten);
		if (this.remoteChannel.readListener != null && bytesWritten > 0) {
			this.remoteChannel.readListener.available();
		}
	}

}
