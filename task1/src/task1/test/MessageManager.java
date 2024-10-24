package task1.test;

import java.nio.charset.StandardCharsets;

import task1.abstracts.Channel;
import task1.implement.ChannelDisconnectedException;

public class MessageManager {
	private Channel channel;
	
	public MessageManager(Channel channel) {
		this.channel = channel;
	}
	
	private int byteArrayToInt(byte[] byteBuffer) {
		return ((byteBuffer[0] & 0xFF) << 24) | ((byteBuffer[1] & 0xFF) << 16) | ((byteBuffer[2] & 0xFF) << 8) | (byteBuffer[3] & 0xFF);
	}
	
	private int readMessageLength(){
		int readedBytes = 0;
		byte[] messageSizeBuffer = new byte[4];
		Logger.debug("Start reading message length.");
		while(readedBytes < 4) {
			try {
				int actualReadedBytes = this.channel.read(messageSizeBuffer, readedBytes, 4 - readedBytes);
				if(actualReadedBytes < 0) {
					Logger.error("Channel.read() returned negative value in readMessageLength");
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
		Logger.debug("Message length readed : " + messageLength);
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
		Logger.debug("Start sending message length.");
		while(writedBytes < 4) {
			try {
				int actualWritedBytes = this.channel.write(messageSizeBuffer, writedBytes, messageLength - writedBytes);
				if(actualWritedBytes < 0) {
					Logger.error("Channel.write() returned negative value in writeMessageLength");
					return;
				}
				else {
					writedBytes += actualWritedBytes;
				}
			} catch (ChannelDisconnectedException e) {
				e.printStackTrace();
			}
		}
		Logger.debug("Sended message length : " + messageLength);
	}
	
	public String readMessage() {
		int messageLength = readMessageLength();
		if(messageLength < 1) {
			return null;
		}
		else {
			int readedBytes = 0;
			byte[] messageBuffer = new byte[messageLength];
			Logger.debug("Start reading message of " + messageLength + " bytes.");
			while(readedBytes < messageLength) {
				try {
					int actualReadedBytes = this.channel.read(messageBuffer, readedBytes, messageLength - readedBytes);
					if(actualReadedBytes < 0) {
						Logger.error("Channel.read() returned negative value with a messageLength of : " + messageLength);
						return null;
					}
					else {
						readedBytes += actualReadedBytes;
					}
				} catch (ChannelDisconnectedException e) {
					e.printStackTrace();
				}
			}
			String message = new String(messageBuffer, StandardCharsets.UTF_8); 
			Logger.debug("Reading message : " + message);
			return message;
		}
	}
	
	public void writeMessage(String message) {
		byte[] messageBuffer = message.getBytes();
		int messageLength = messageBuffer.length;
		writeMessageLength(messageLength);
		int writedBytes = 0;
		Logger.debug("Start writing message : " + message);
		while(writedBytes < messageLength) {
			try {
				int actualWritedBytes = this.channel.write(messageBuffer, writedBytes, messageLength - writedBytes);
				if (actualWritedBytes < 0) {
					Logger.error("Channel.write() returned negative value in writeMessage");
					return;
				}
				else {
					writedBytes += actualWritedBytes;
				}
			} catch (ChannelDisconnectedException e) {
				e.printStackTrace();
			}
		}
		Logger.debug("Sended message : " + message);
	}
}
