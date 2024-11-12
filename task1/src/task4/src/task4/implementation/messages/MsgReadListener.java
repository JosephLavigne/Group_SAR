/*package task4.implementation.messages;

import task4.abs.QueueChannelListener;
import task4.implementation.ChannelImplementation;

public class MsgReadListener {
	
	private int msgOffset;
	private byte[] readData;
	private byte[] msgLengthBuffer;
	private ChannelImplementation channel;
	
	public QueueChannelListener queueChannelListener;

	private ListenerState state;

	public MsgReadListener(ChannelImplementation channel) {
		this.msgOffset = 0;
		this.readData = null;
		this.channel = channel;
		this.queueChannelListener = null;
		this.state = ListenerState.READING_SIZE;
	}

	public void available() {
		if (this.state == ListenerState.READING_SIZE) {
			int readBytes = this.channel.read(this.msgLengthBuffer, this.msgOffset, Message.MSG_LENGTH_SIZE - this.msgOffset);
			this.msgOffset += readBytes;
			if (this.msgOffset == 4) {
				int msgSize = convertByteToInt(this.msgLengthBuffer);
				this.readData = new byte[msgSize];
				this.msgOffset = 0;
				this.state = ListenerState.READING_MSG;
			}
		}
		if (this.state == ListenerState.READING_MSG) {
			int readBytes = this.channel.read(this.readData, this.msgOffset, this.readData.length - this.msgOffset);
			this.msgOffset += readBytes;
			if (this.msgOffset == this.readData.length) {
				this.msgOffset = 0;
				this.state = ListenerState.READING_SIZE;
				queueChannelListener.received(this.readData);
			}
		}
	}
	
	private int convertByteToInt(byte[] byteArray) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result += (byteArray[i] & 0xFF) << (i * 8);
		}
		return result;
	}


}*/
