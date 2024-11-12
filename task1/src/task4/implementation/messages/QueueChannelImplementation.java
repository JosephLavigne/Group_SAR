package task4.implementation.messages;

import java.util.LinkedList;

import task4.abs.Channel;
import task4.abs.Channel.DisconnectListener;
import task4.abs.Channel.ReadListener;
import task4.abs.Channel.WriteListener;
import task4.abs.QueueChannel;
import task4.abs.QueueChannelListener;
import task4.implementation.TaskImplementation;

public class QueueChannelImplementation extends QueueChannel {

	private Channel channel;

	private MsgReadListener msgReadListener;

	private QueueChannelListener listener;

	private byte[] length;
	private TaskImplementation writeTask;
	private MsgWriteListener msgWriteListener;
	private Message currentWritingMessage;
	private LinkedList<Message> messagesList;
	private Runnable startSending;

	public QueueChannelImplementation(Channel channel) {
		this.channel = channel;
		this.msgReadListener = new MsgReadListener();
		this.writeTask = new TaskImplementation("Write Task");
		this.currentWritingMessage = null;
		this.msgWriteListener = new MsgWriteListener();
		this.messagesList = new LinkedList<>();
		this.length = new byte[4];

		startSending = new Runnable() {
			@Override
			public void run() {
				if (currentWritingMessage == null)
					return;
					
				for (int i = 0; i < 4; i++) {
					length[i] = (byte) ((currentWritingMessage.getLength() >> (i * 8)) & 0xFF);
				}
				channel.write(length, 0, 4, msgWriteListener);
			}
		};
	}

	@Override
	public void setListener(QueueChannelListener listener) {
		// TODO Auto-generated method stub
		this.listener = listener;
		this.msgReadListener.setListener(listener);
		channel.setReadListener(this.msgReadListener);
		this.msgWriteListener.setListener(listener);
	}

	@Override
	public boolean send(Message message) {
		// TODO Auto-generated method stub
		if (channel.disconnected()) {
			return false;
		}

		if (this.currentWritingMessage != null) {
			this.messagesList.add(message);
		} else {
			this.currentWritingMessage = message;
			this.writeTask.postRunnable(startSending);
		}

		return true;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		DisconnectListener disconnectListener = new DisconnectListener() {
			@Override
			public void disconnected() {
				listener.isQueueChannelClosed();
			}
		};

		this.channel.disconnect(disconnectListener);
	}

	@Override
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return this.channel.disconnected();
	}
	
	public class MsgWriteListener implements WriteListener{
		
		private int msgOffset;
		private byte[] msgLengthBuffer;

		public QueueChannelListener queueChannelListener;
		private ListenerState state;

		public MsgWriteListener() {
			this.msgLengthBuffer = new byte[4];
			this.msgOffset = 0;
			this.queueChannelListener = null;
			this.state = ListenerState.WRITING_SIZE;
		}
		
		void setListener(QueueChannelListener listener) {
			this.queueChannelListener = listener;
		}

		public void writtenBytes(int bytesWritten) {
			if (channel.disconnected()) {
				return;
			}
			
			this.msgOffset += bytesWritten;
			if (this.state == ListenerState.WRITING_SIZE) {
	            if (this.msgOffset == 4) {
	            	this.msgOffset = 0;
	                this.state = ListenerState.WRITING_MSG;
	                channel.write(currentWritingMessage.getBytes(), 0, currentWritingMessage.getLength(), this);
	            } else {
	                channel.write(this.msgLengthBuffer, this.msgOffset, Message.MSG_LENGTH_SIZE - this.msgOffset, this);
	            }
	        }
			
			if(this.state == ListenerState.WRITING_MSG) {
	                if (this.msgOffset == currentWritingMessage.getLength()) {
	                	this.msgOffset = 0;
	                	this.state = ListenerState.WRITING_SIZE;
	                    this.queueChannelListener.sent(currentWritingMessage);
	                    currentWritingMessage = null;
	                    if (!messagesList.isEmpty()) {
	                    	messagesList.poll();
	                    	writeTask.postRunnable(startSending);
	                    }
	                } else {
	                    channel.write(currentWritingMessage.getBytes(), this.msgOffset, currentWritingMessage.getLength() - this.msgOffset, this);
	                }
	            }
			}
	}

	private class MsgReadListener implements ReadListener{
		private int msgOffset;
		private byte[] readData;
		private byte[] msgLengthBuffer;
		
		public QueueChannelListener queueChannelListener;

		private ListenerState state;

		public MsgReadListener() {
			this.msgOffset = 0;
			this.readData = null;
			this.queueChannelListener = null;
			this.state = ListenerState.READING_SIZE;
		}
		
		void setListener(QueueChannelListener listener) {
			this.queueChannelListener = listener;
		}

		public void available() {
			if (this.state == ListenerState.READING_SIZE) {
				int readBytes = channel.read(this.msgLengthBuffer, this.msgOffset, Message.MSG_LENGTH_SIZE - this.msgOffset);
				this.msgOffset += readBytes;
				if (this.msgOffset == 4) {
					int msgSize = convertByteToInt(this.msgLengthBuffer);
					this.readData = new byte[msgSize];
					this.msgOffset = 0;
					this.state = ListenerState.READING_MSG;
				}
			}
			if (this.state == ListenerState.READING_MSG) {
				int readBytes = channel.read(this.readData, this.msgOffset, this.readData.length - this.msgOffset);
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
	}

}
