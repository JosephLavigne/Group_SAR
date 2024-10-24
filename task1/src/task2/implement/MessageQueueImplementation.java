package task2.implement;

import task1.abstracts.Channel;
import task1.implement.ChannelDisconnectedException;
import task2.abstracts.MessageQueue;

public class MessageQueueImplementation extends MessageQueue{
	
	private Channel channel;
	
	public MessageQueueImplementation(Channel channel) {
		this.channel = channel;
	}
	
	private int byteArrayToInt(byte[] byteBuffer) {
		return ((byteBuffer[0] & 0xFF) << 24) | ((byteBuffer[1] & 0xFF) << 16) | ((byteBuffer[2] & 0xFF) << 8) | (byteBuffer[3] & 0xFF);
	}
	
	private int readMessageLength(){
		int readedBytes = 0;
		byte[] messageSizeBuffer = new byte[4];
		while(readedBytes < 4) {
			try {
				int actualReadedBytes = this.channel.read(messageSizeBuffer, readedBytes, 4 - readedBytes);
				if(actualReadedBytes < 0) {
					return -1;
				}
				else {
					readedBytes += actualReadedBytes;
				}
			} catch (ChannelDisconnectedException e) {
				e.printStackTrace();
			}
		}
		int messageLength = byteArrayToInt(messageSizeBuffer);
		return messageLength;
	}
	
	private byte[] intToByteArray(int value) {
		byte[] byteBuffer = new byte[4];
		byteBuffer[0] = (byte) ((value >> 24) & 0xFF);
		byteBuffer[1] = (byte) ((value >> 16) & 0xFF);
		byteBuffer[2] = (byte) ((value >> 8) & 0xFF);
		byteBuffer[3] = (byte) (value & 0xFF);
		return byteBuffer;
	}
	
	private void writeMessageLength(int messageLength) {
		int writedBytes = 0;
		byte[] messageSizeBuffer = this.intToByteArray(messageLength);
		while(writedBytes < 4) {
			try {
				int actualWritedBytes = this.channel.write(messageSizeBuffer, writedBytes, messageLength - writedBytes);
				if(actualWritedBytes < 0) {
					return;
				}
				else {
					writedBytes += actualWritedBytes;
				}
			} catch (ChannelDisconnectedException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Send fait une copie du tableau de bytes. Le tableau de bytes dont on donne la référence en paramètre
	 * peut être modifié sans risque.	
	 */
	@Override
	public void send(byte[] bytes, int offset, int length) {
		// TODO Auto-generated method stub
		byte[] messageBuffer = new byte[bytes.length];
		System.arraycopy(bytes, 0, messageBuffer, 0, bytes.length);
		int messageLength = messageBuffer.length;
		writeMessageLength(messageLength);
		int writedBytes = 0;
		while(writedBytes < messageLength) {
			try {
				int actualWritedBytes = this.channel.write(messageBuffer, writedBytes, messageLength - writedBytes);
				if (actualWritedBytes < 0) {
					return;
				}
				else {
					writedBytes += actualWritedBytes;
				}
			} catch (ChannelDisconnectedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public byte[] receive() {
		// TODO Auto-generated method stub
		int messageLength = this.readMessageLength();
		if(messageLength <= 0) {
			return null;
		}
		int readedBytes = 0;
		byte[] messageBuffer = new byte[messageLength];
		while(readedBytes < messageLength) {
			try {
				int actualReadedBytes = this.channel.read(messageBuffer, readedBytes, messageLength - readedBytes);
				if(actualReadedBytes < 0) {
					return null;
				}
				else {
					readedBytes += actualReadedBytes;
				}
			} catch (ChannelDisconnectedException e) {
				e.printStackTrace();
			}
		}
		return messageBuffer;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		this.channel.disconnect();
	}

	@Override
	public boolean closed() {
		// TODO Auto-generated method stub
		return this.channel.disconnected();
	}

}
